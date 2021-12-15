package com.itheima.document.client;

import com.itheima.common.vo.PageResult;
import com.itheima.common.vo.Result;
import com.itheima.document.entity.Category;
import com.itheima.document.entity.File;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("document-service")
public interface DocumentClient {

    /**
     * 仅提供测试 导入数据到索引库
     * 分页查询数据库中所有企业文档
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/document/page")
    public PageResult<File> queryByPage(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize);

    /**
     * 根据分类id集合查询分类
     * @param ids
     * @return
     */
    @GetMapping("/category/list")
    public Result<List<Category>> queryCategoryByIds(@RequestParam("ids") List<Long> ids);

}