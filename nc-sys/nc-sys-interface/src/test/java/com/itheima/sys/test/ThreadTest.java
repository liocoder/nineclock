package com.itheima.sys.test;

public class ThreadTest {

    public static void main(String[] args) {
        System.out.println("main.start");
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread() + "执行");
            }
        });
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main.end");
    }
}
