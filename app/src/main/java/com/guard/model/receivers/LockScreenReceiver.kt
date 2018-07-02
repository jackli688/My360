package com.guard.model.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.guard.model.utils.ProcessUtil

class LockScreenReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        ProcessUtil.killProcesses()
    }

}