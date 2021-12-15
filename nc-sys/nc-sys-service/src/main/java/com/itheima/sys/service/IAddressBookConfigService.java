package com.itheima.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.sys.entity.AddressBookConfig;

import java.util.List;

/**
 * <p>
 * 企业通讯录字段表 服务类
 * </p>
 *
 * @author itheima
 * @since 2021-11-23
 */
public interface IAddressBookConfigService extends IService<AddressBookConfig> {

    List<AddressBookConfig> queryAddressBookInfo();

    void saveAddressBook(AddressBookConfig addressBookConfig);

    void updateContactConfig(AddressBookConfig addressBookConfig);
}
