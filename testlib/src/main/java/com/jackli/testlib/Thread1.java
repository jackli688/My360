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
public class Thread1 extends Thread {

    private Locked mLock;

    public Thread1(String name, Locked l) {
        super(name);
        this.mLock = l;
    }

    @Override
    public void run() {
        super.run();
        synchronized (mLock) {
            for (int i = 0; i < 3; i++) {
                System.out.println("Thread1 run" + i);
            }
            System.out.println("A 1");
            try {
                mLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("A 2");
            System.out.println("A 3");
        }

    }
}
