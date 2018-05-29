package com.guard.ui.activities

import android.util.Log
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.ui.activities
 * @description: description
 * @date: 2018/5/19
 * @time: 0:30
 */
class SplashActivityTest : CallBack {

    @Test
    override fun succeeded(result: Int) {
        Log.e("lijiwei", "当前线程的名字:${Thread.currentThread().name}")
        Assert.assertEquals(0, 1)
        System.out.println("当前线程的名字:${Thread.currentThread().name}")
    }

    @Test
    override fun failure() {
        Log.e("lijiwei", "当前线程的名字:${Thread.currentThread().name}")
        Assert.assertEquals(0, 1)
    }

    @Test
    fun onCreate() {
        var myThread = MyThread()
        assertNotNull(myThread)
        myThread.mInterface = this@SplashActivityTest
        assertNotNull(myThread.mInterface)
        myThread.start()
    }


}