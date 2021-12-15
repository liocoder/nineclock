package com.itheima.document.task;

import cn.itcast.aliyun.template.AliyunGreenTemplate;
import com.itheima.document.entity.File;
import com.itheima.document.service.IFileService;
import com.itheima.sys.client.SysClient;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.itheima.document.enums.DocumentEnum.*;

@Slf4j
@Component
public class DocApproveTask {

    @Autowired
    private IFileService fileService;

    @Autowired
    private AliyunGreenTemplate aliyunGreenTemplate;

    @Autowired
    private SysClient sysClient;

    @Autowired
    private RedissonClient redissonClient;

    @Scheduled(cron = "0/30 * * * * ?")
    public void queryDocApprove() {
        RLock lock = null;
        try {
            lock = redissonClient.getLock("lock");
            //尝试获取锁
            boolean b = lock.tryLock(5, 5, TimeUnit.SECONDS);
            if (!b) {
                log.error("获取锁失败，下次尝试获取！");
                return;
            }

            log.info("执行定时任务：定时查询待审核文档");
            //1. 检索待审核文档
            List<File> fileList = fileService.queryFilesByStatus(FILE_STATUS_WAIT_APPROVE.getVal());
            if (!CollectionUtils.isEmpty(fileList)) {
                //2. 遍历文档进行审核
                for (File file : fileList) {
                    String content = file.getName() + file.getContent();
                    Map<String, String> map = aliyunGreenTemplate.greenTextScan(content);
                    //处理审核结果
                    String suggestion = map.get("suggestion");
                    if ("pass".equals(suggestion)) {
                        //TODO　更新文档状态:已发布, 远侧调用“积分”微服务给用户增加积分
                        sysClient.updateIntegral(file.getCompanyUserId(), true, 10);
                        file.setStatus(FILE_STATUS_PASS.getVal());
                    } else {
                        //更新文档状态：人工审核
                        file.setStatus(FILE_STATUS_APPROVE_ARTIFICIAL.getVal());
                    }
                    fileService.updateById(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放锁
            if (lock != null) {
                lock.unlock();
            }
        }

    }
}
