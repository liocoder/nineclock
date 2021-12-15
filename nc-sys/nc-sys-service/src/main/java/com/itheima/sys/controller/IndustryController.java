package com.itheima.sys.controller;

import com.itheima.common.vo.Result;
import com.itheima.sys.service.ICommonIndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 行业
 */
@RestController
public class IndustryController {

    @Autowired
    private ICommonIndustryService industryService;

    /**
     * 检索行业列表数据
     * GET/sys/industry/list
     */
    @GetMapping("/industry/list")
    public Result industryList(@RequestParam(value = "parentId", required = false) String parentId) {
        return Result.success(industryService.industryList(parentId));
    }
}
