package com.itheima.sys.controller;

import com.itheima.common.vo.Result;
import com.itheima.sys.entity.Company;
import com.itheima.sys.service.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class CompanyController {

    @Autowired
    private ICompanyService companyService;

    /**
     * 企业-获取企业信息
     * GET/sys/company/company
     */
    @GetMapping("/company/company")
    public Result companyInfo() {
        return Result.success(companyService.companyInfo());
    }

    /**
     * 图片上传
     * POST/sys/company/uploadOSS
     */
    @PostMapping("/company/uploadOSS")
    public Result uploadLogo(MultipartFile file) throws IOException {
        return Result.success(companyService.uploadLogo(file));
    }

    /**
     * 修改企业信息
     * PUT/sys/company/company
     */
    @PutMapping("/company/company")
    public Result updateCompany(@RequestBody Company company) {
        companyService.updateCompany(company);
        return Result.success();
    }

    /**
     * 根据关键字查询企业
     * GET/sys/company/list
     */
    @GetMapping("/company/list")
    public Result companyByKeyword(String keyword, String industryId) {
        return Result.success(companyService.companyByKeyword(keyword, industryId));
    }

}
