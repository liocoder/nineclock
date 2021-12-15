package com.itheima.sys.test;

public class ThreadImpl extends Thread {
    //共享锁
    private static Object lock = new Object();
    //定义打印次序
    private static int currentCount = 0;
    //记录当前打印的值
    private int flag;
    private String value;

    public ThreadImpl(int flag, String value) {
        this.flag = flag;
        this.value = value;
    }

    @Override
    public void run() {
            synchronized (lock) {
                while (currentCount % 3 != flag) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //本线程打印
                System.out.printf("%s, %s\n",value, Thread.currentThread().getName());
                currentCount++;
                lock.notifyAll();
            }
    }
    public static void main(String[] args) {
        Thread a = new ThreadImpl(0, "A");
        Thread b = new ThreadImpl(1, "B");
        Thread c = new ThreadImpl(2, "C");
        a.start();
        b.start();
        c.start();
    }
}
