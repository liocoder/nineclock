package com.itheima.document.controller;

import com.itheima.common.vo.Result;
import com.itheima.document.entity.Category;
import com.itheima.document.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 提供分类
 */
@RestController
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    /**
     * 根据分类id集合查询分类集合
     * GET /category/list
     */
    @GetMapping("/category/list")
    public Result<List<Category>> queryCategoryByIds(@RequestParam("ids") List<Long> ids) {
        return Result.success((List<Category>) categoryService.findCategoryList(ids));
    }
}
