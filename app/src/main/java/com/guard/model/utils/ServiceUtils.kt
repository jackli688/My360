package com.guard.model.utils

import android.app.ActivityManager
import android.content.Context

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.model.utils
 * @description: description
 * @date: 2018/6/4
 * @time: 12:20
 */
class ServiceUtils {

    companion object {
        @Suppress("DEPRECATION")
        fun checkServiceIsRunning(context: Context, className: String): Boolean {
            val manager = context.getSystemService(android.content.Context.ACTIVITY_SERVICE) as ActivityManager
            manager.getRunningServices(Int.MAX_VALUE).forEach { runningService ->
                val service = runningService.service
                if (service.className == className) {
                    return true
                }
            }
            return false
        }
    }

}