package com.guard.model.utils

import android.content.Context
import android.content.pm.PackageManager

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.model
 * @description: description
 * @date: 2018/5/6
 * @time: 15:20
 */
class PackageUtil {

    object version {
        fun getVersionCode(context: Context): Int {
            val manager = getPackagemanager(context)
            val info = manager.getPackageInfo(context.packageName, 0)
            return info.versionCode
        }

        fun getPackagemanager(context: Context): PackageManager {
            return context.packageManager

        }

        fun getVersionName(context: Context): String {
            val manager = getPackagemanager(context)
            val info = manager.getPackageInfo(context.packageName, 0)
            return info.versionName
        }
    }
}