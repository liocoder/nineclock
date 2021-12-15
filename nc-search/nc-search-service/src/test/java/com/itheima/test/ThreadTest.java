package com.itheima.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ThreadTest {


    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        String ss="111111";
        String c = ss;
        System.out.println(c == ss);
        Class reflect=ss.getClass();
        Field f=reflect.getDeclaredField("value");
        f.setAccessible(true);
        char [] b = (char[]) f.get(ss);
        b[0] = 'a';
        System.out.println(c == ss);
        System.out.println(ss);

    }
}
