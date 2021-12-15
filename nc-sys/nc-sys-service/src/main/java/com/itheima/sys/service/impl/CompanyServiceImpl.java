package com.itheima.sys.service.impl;

import cn.itcast.aliyun.template.AliyunOssTemplate;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.common.enums.ResponseEnum;
import com.itheima.common.exception.auth.NcException;
import com.itheima.common.threadlocals.UserHolder;
import com.itheima.sys.dto.UserJoinCompanyDTO;
import com.itheima.sys.entity.Company;
import com.itheima.sys.entity.CompanyUser;
import com.itheima.sys.mapper.CompanyMapper;
import com.itheima.sys.service.ICompanyService;
import com.itheima.sys.service.ICompanyUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 企业表 服务实现类
 * </p>
 *
 * @author itheima
 * @since 2021-11-23
 */
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements ICompanyService {

    @Autowired
    private AliyunOssTemplate ossTemplate;
    @Autowired
    private ICompanyUserService userService;

    public Company companyInfo() {
        Long companyId = UserHolder.getCompanyId();
        LambdaQueryWrapper<Company> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Company::getId, Company::getName, Company::getLogo);
        wrapper.eq(Company::getId, companyId);
        return this.getOne(wrapper);
    }

    //允许图片上传的类型
    private static final List<String> allowTypeList = Arrays.asList("image/jpeg", "image/bmp", "image/png", "application/octet-stream");
    //logo上传
    @Override
    public String uploadLogo(MultipartFile file) throws IOException {
        //判断图片格式是否正确
        String contentType = file.getContentType();
        if (!allowTypeList.contains(contentType)) {
            throw new NcException(ResponseEnum.INVALID_FILE_TYPE);
        }
        try {
            //ImageIO判断是否为图片内容
            BufferedImage read = ImageIO.read(file.getInputStream());
            if (read == null) {
                throw new NcException(ResponseEnum.INVALID_FILE_TYPE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new NcException(ResponseEnum.INVALID_FILE_TYPE);
        }
        //文件上传
        return ossTemplate.upload4inputStream(file.getOriginalFilename(), file.getInputStream());
    }

    @Override
    public void updateCompany(Company company) {
        Long companyId = UserHolder.getCompanyId();
        company.setId(companyId);
        //修改企业信息需要重新认证
        company.setAuditStatus(0);
        //执行更新
        this.updateById(company);
        //更新用户表中企业字段信息
        LambdaUpdateWrapper<CompanyUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CompanyUser::getCompanyId, companyId);
        wrapper.set(CompanyUser::getCompanyName, company.getName());
        userService.update(wrapper);
    }

    @Override
    public List<Company> companyByKeyword(String keyword, String industryId) {
        //1.业务要求，不能全表扫描企业列表
        if(StringUtils.isBlank(keyword) && StringUtils.isBlank(industryId)){
            throw new NcException(ResponseEnum.INVALID_PARAM_ERROR);
        }
        LambdaQueryWrapper<Company> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(industryId)) {
            wrapper.eq(Company::getIndustryId, industryId);
        }
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.like(Company::getName, keyword);
        }
        wrapper.select(Company::getId, Company::getName);
        return this.list(wrapper);
    }
}
