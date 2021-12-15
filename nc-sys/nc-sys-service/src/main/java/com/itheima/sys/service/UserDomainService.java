package com.itheima.sys.service;

import com.itheima.sys.entity.UserDomain;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Service;

@Service
public class UserDomainService {

    public UserDomain saveUserDomain(UserDomain userDomain) {
        userDomain.setId(RandomUtils.nextLong());
        return userDomain;
    }
}
