package com.itheima.search.controller;

import com.itheima.common.vo.PageResult;
import com.itheima.common.vo.Result;
import com.itheima.search.dto.SearchDTO;
import com.itheima.search.entity.Article;
import com.itheima.search.service.DocSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class SearchController {

    @Autowired
    private DocSearchService searchService;

    /**
     * 文档全文检索
     * POST/search/page
     */
    @PostMapping("/search/page")
    public Result<PageResult<Article>> searchByKeyword(@RequestBody SearchDTO searchDTO) throws IOException {
        return Result.success(searchService.searchByKeyword(searchDTO));
    }

    /**
     * 根据关键字检索索引库中的文档数据-进行聚合索引
     * POST /search/filter
     *
     * @return {"分类":[{id:7,name:技术文章},{id:2,name:技术文档}]}
     */
    @PostMapping("/search/filter")
    public Result<Map<String, List<?>>> searchFilter(@RequestBody SearchDTO searchDTO) throws IOException {
        return Result.success(searchService.searchFilter(searchDTO));
    }
}
