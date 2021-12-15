package com.itheima.sys.test;

import org.apache.logging.log4j.util.Strings;
import org.junit.Test;

public class RoleIdTest {

    @Test
    public void test() {
        String ids = "1";
        String[] idStr = ids.split(",");
        System.out.println(idStr[0]);
    }

    @Test
    public void testStringFunction() {
        String str = "";
        String str1 = null;
        String str2 = " ";
        System.out.println(Strings.isBlank(str));
//        System.out.println(Strings.isBlank(str1));
        System.out.println(Strings.isBlank(str2));
        System.out.println(Strings.isEmpty(str));
        System.out.println(Strings.isEmpty(str2));
    }
}
