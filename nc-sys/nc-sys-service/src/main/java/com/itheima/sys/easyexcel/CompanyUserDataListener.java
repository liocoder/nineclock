package com.itheima.sys.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import com.itheima.common.threadlocals.UserHolder;
import com.itheima.sys.entity.CompanyUser;
import com.itheima.sys.service.ICompanyUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 有个很重要的点 CompanyUserDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
 */
@Slf4j
public class CompanyUserDataListener implements ReadListener<CompanyUser> {

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    /**
     * 缓存的数据
     */
    private List<CompanyUser> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    private ICompanyUserService companyUserService;

//    public CompanyUserDataListener() {}

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param companyUserService
     */
    public CompanyUserDataListener(ICompanyUserService companyUserService) {
        this.companyUserService = companyUserService;
    }

    /**
     * 这个每一条数据解析都会来调用
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(CompanyUser data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        Boolean flag = companyUserService.checkMobile(data.getMobile());
        if(flag){
            //手机号已存在的数据则不处理
            return;
        }
        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            //存储完清理缓存
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     * 如果最后只剩下4个数据，不够五个，则在此方法中调用saveData方法写入
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库：将员工数据批量导入数据库中 ，将员工关联到当前企业
     */
    private void saveData() {
        if(!CollectionUtils.isEmpty(cachedDataList)){
            log.info("{}条数据，开始存储数据库！", cachedDataList.size());
            for (CompanyUser companyUser : cachedDataList) {
                companyUser.setCompanyId(UserHolder.getCompanyId());
                companyUser.setCompanyName(UserHolder.getCompanyName());
                companyUser.setEnable(true);
            }
            companyUserService.saveBatch(cachedDataList);
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
            log.info("存储数据库成功！");
        }
    }
}
