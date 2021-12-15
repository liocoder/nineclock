package com.itheima.sys.controller;

import com.itheima.common.enums.ResponseEnum;
import com.itheima.common.exception.auth.NcException;
import com.itheima.common.vo.Result;
import com.itheima.sys.entity.AddressBookConfig;
import com.itheima.sys.service.IAddressBookConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 通讯录
 */
@RestController
public class AddressBookConfigController {

    @Autowired
    private IAddressBookConfigService configService;

    /**
     * 企业通讯录-查询通讯录或初始化通讯录
     * GET/sys/company/config/contact
     */
    @GetMapping("/company/config/contact")
    public Result queryAddressBookInfo() {
        return Result.success(configService.queryAddressBookInfo());
    }

    /**
     * 企业通讯录-添加通讯录字段
     * POST/sys/company/config/contact
     */
    @PostMapping("/company/config/contact")
    public Result saveAddressBook(@RequestBody AddressBookConfig addressBookConfig) {
        configService.saveAddressBook(addressBookConfig);
        return Result.success("新增通讯录字段成功");
    }

    /**
     * 企业通讯录-修改通讯录字段字段状态
     * PUT/sys/company/config/contact/:id/:type
     */
    @PutMapping("/company/config/contact/{id}/{type}")
    public Result updateContactConfig(@RequestBody AddressBookConfig addressBookConfig) {
        configService.updateContactConfig(addressBookConfig);
        return Result.success("修改成功");
    }

    /**
     * 企业通讯录-删除通讯录字段
     * DELETE/company/config/contact/:id
     */
    @DeleteMapping("/company/config/contact/{id}")
    public Result deleteContactConfig(@PathVariable("id") Long id) {
        boolean b = configService.removeById(id);
        if (!b) {
            throw new NcException(ResponseEnum.DELETE_OPERATION_FAIL);
        }
        return Result.success("删除操作成功");
    }
}
