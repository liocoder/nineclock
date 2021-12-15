package com.itheima.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.sys.entity.Company;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 企业表 服务类
 * </p>
 *
 * @author itheima
 * @since 2021-11-23
 */
public interface ICompanyService extends IService<Company> {

    Company companyInfo();

    String uploadLogo(MultipartFile file) throws IOException;

    void updateCompany(Company company);

    List<Company> companyByKeyword(String keyword, String industryId);

}
