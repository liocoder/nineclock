package com.itheima.auth.client;

import com.itheima.common.vo.Result;
import com.itheima.sys.client.SysClient;
import com.itheima.sys.dto.CompanyUserDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SysClientTest {

    @Autowired
    private SysClient sysClient;

    @Test
    public void queryUser() {
        Result<CompanyUserDTO> result = sysClient.querySysUser("13100131000");
        System.out.println(result);
    }
}
