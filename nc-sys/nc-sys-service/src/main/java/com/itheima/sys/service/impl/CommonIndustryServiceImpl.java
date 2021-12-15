package com.itheima.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.sys.entity.CommonIndustry;
import com.itheima.sys.mapper.CommonIndustryMapper;
import com.itheima.sys.service.ICommonIndustryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 行业字典表 企业-行业字典表 服务实现类
 * </p>
 *
 * @author itheima
 * @since 2021-11-23
 */
@Service
public class CommonIndustryServiceImpl extends ServiceImpl<CommonIndustryMapper, CommonIndustry> implements ICommonIndustryService {

    @Override
    public List<CommonIndustry> industryList(String parentId) {
        LambdaQueryWrapper<CommonIndustry> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommonIndustry::getStatus, true);    //可用
        if (StringUtils.isBlank(parentId)) {
            wrapper.isNull(CommonIndustry::getParentId);
        } else {
            wrapper.eq(CommonIndustry::getParentId, parentId);
        }
        return this.list(wrapper);
    }
}
