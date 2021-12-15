package com.itheima.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itheima.sys.entity.Company;
import com.itheima.sys.service.ICompanyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataTest {
    @Autowired
    private ICompanyService iCompanyService;
    @Test
    public void testData() {
        QueryWrapper<Company> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Company::getName, "北京-传智");

        List<Company> list = iCompanyService.list(wrapper);
        System.out.println(list);
    }
}
