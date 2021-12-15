package com.itheima.document.service;

import com.itheima.document.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author itheima
 * @since 2021-11-29
 */
public interface ICategoryService extends IService<Category> {

    List<Category> findCategoryList(List<Long> categoryIds);
}
