package com.itheima.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.common.enums.ResponseEnum;
import com.itheima.common.exception.auth.NcException;
import com.itheima.common.threadlocals.UserHolder;
import com.itheima.sys.entity.AddressBookConfig;
import com.itheima.sys.mapper.AddressBookConfigMapper;
import com.itheima.sys.service.IAddressBookConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 企业通讯录字段表 服务实现类
 * </p>
 *
 * @author itheima
 * @since 2021-11-23
 */
@Service
public class AddressBookConfigServiceImpl extends ServiceImpl<AddressBookConfigMapper, AddressBookConfig> implements IAddressBookConfigService {

    /**
     * 查询/初始化新增   通讯录字段列表
     */
    @Override
    public List<AddressBookConfig> queryAddressBookInfo() {
        //企业id
        Long companyId = UserHolder.getCompanyId();
        LambdaQueryWrapper<AddressBookConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBookConfig::getCompanyId, companyId);
        int count = this.count(wrapper);
        if (count == 0) {
            //如果没有列表，新增默认项
            String[] items = new String[]{"部门", "职位", "姓名", "工号", "手机号", "邮箱", "座机", "办公地点", "备注", "入职时间"};
            List<AddressBookConfig> list = new ArrayList<>();
            for (String itemName : items) {
                AddressBookConfig addressBookConfig = new AddressBookConfig();
                addressBookConfig.setCompanyId(UserHolder.getCompanyId());
                addressBookConfig.setName(itemName);
                addressBookConfig.setStatus(true);
                addressBookConfig.setType("fixed");
                addressBookConfig.setFieldType("string");
                list.add(addressBookConfig);
            }
            //执行批量保存
            this.saveBatch(list);
            return list;
        } else {
            return this.list(wrapper);
        }
    }

    @Override
    public void saveAddressBook(AddressBookConfig addressBookConfig) {
        Long companyId = UserHolder.getCompanyId();
        //判断信息项是否存在
        LambdaQueryWrapper<AddressBookConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBookConfig::getCompanyId, companyId);
        wrapper.eq(AddressBookConfig::getName, addressBookConfig.getName());
        int count = this.count(wrapper);
        if (count > 0) {
            throw new NcException(ResponseEnum.INVALID_PARAM_ERROR);
        }
        //增加信息项
        AddressBookConfig companyContactConfig = new AddressBookConfig();
        companyContactConfig.setType("dynamic");
        companyContactConfig.setName(addressBookConfig.getName());
        companyContactConfig.setStatus(false);
        companyContactConfig.setFieldType("string");
        companyContactConfig.setCompanyId(companyId);
        boolean b = this.save(companyContactConfig);
        if (!b) {
            throw new NcException(ResponseEnum.INSERT_OPERATION_FAIL);
        }


    }

    @Transactional
    @Override
    public void updateContactConfig(AddressBookConfig addressBookConfig) {
        Long companyId = UserHolder.getCompanyId();
        addressBookConfig.setCompanyId(companyId);
        boolean b = this.updateById(addressBookConfig);
        if (!b) {
            throw new NcException(ResponseEnum.INSERT_OPERATION_FAIL);
        }
    }
}
