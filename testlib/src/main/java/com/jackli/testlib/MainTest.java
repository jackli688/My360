package com.jackli.testlib;

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.jackli.testlib
 * @description: description
 * @date: 2018/5/19
 * @time: 14:19
 */
public class MainTest {
    public static void main(String[] args) {
        Locked locked = new Locked();
        Thread1 thread1 = new Thread1("thread1", locked);
        Thread2 thread2 = new Thread2("thread2", locked);
        thread1.start();
        thread2.start();
    }


}
