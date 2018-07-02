package com.guard.model.services

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.guard.model.receivers.LockScreenReceiver

class LockScreenService : Service() {
    lateinit var lockScreenReceiver: LockScreenReceiver
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onCreate() {
        //锁屏清理进程
        //1.广播接收者
        lockScreenReceiver = LockScreenReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF) //设置接受的广播事件,ACTION_SCREEN_ON:解锁广播事件
        //注册广播接收者
        registerReceiver(lockScreenReceiver, intentFilter)
    }


    override fun onDestroy() {
        unregisterReceiver(lockScreenReceiver)
    }

}
