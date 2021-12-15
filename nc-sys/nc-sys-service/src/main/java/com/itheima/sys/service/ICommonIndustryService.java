package com.itheima.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.sys.entity.CommonIndustry;

import java.util.List;

/**
 * <p>
 * 行业字典表 企业-行业字典表 服务类
 * </p>
 *
 * @author itheima
 * @since 2021-11-23
 */
public interface ICommonIndustryService extends IService<CommonIndustry> {

    List<CommonIndustry> industryList(String parentId);
}
