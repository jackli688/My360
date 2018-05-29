package com.jackli.testlib;

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.jackli.testlib
 * @description: description
 * @date: 2018/5/19
 * @time: 18:09
 */
public class Thread2 extends Thread {

    private Locked mLock;

    public Thread2(String s, Locked l) {
        super(s);
        this.mLock = l;
    }

    @Override
    public void run() {
        super.run();
        synchronized (mLock) {
            System.out.println("B 1");
            System.out.println("B 2");
            System.out.println("B 3");
            mLock.notify();
        }

    }
}
