package com.itheima.document.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.common.threadlocals.UserHolder;
import com.itheima.document.entity.Category;
import com.itheima.document.mapper.CategoryMapper;
import com.itheima.document.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author itheima
 * @since 2021-11-29
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Override
    public List<Category> findCategoryList(List<Long> categoryIds) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getCompanyId, UserHolder.getCompanyId());
        wrapper.in(Category::getId, categoryIds);
        List<Category> list = this.list(wrapper);
        return list;
    }
}
