package com.guard.model.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES
import android.graphics.drawable.Drawable
import java.io.File

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.model.utils
 * @description: description
 * @date: 2018/6/19
 * @time: 14:59
 */
class AppsUtils {

    companion object {
        fun getAppsMsg(context: Context): List<AppInfo> {
            val pm = context.packageManager
            val applications = pm.getInstalledApplications(GET_UNINSTALLED_PACKAGES)
            val size = applications.size
            val appList = ArrayList<AppInfo>(size)
            for (app in applications) {
                val isSystem = (app.flags and ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM
                val isSDCard = ((app.flags and ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE)
                val file = File(app.sourceDir)
                val appSize = file.length()
                appList.add(AppInfo.Builder()
                        .addName(app.loadLabel(pm).toString() ?: "")
                        .addDrawable(app.loadIcon(pm))
                        .addPackageName(app.packageName)
                        .addSize(appSize)
                        .addIsSD(isSDCard)
                        .addIsSystem(isSystem)
                        .build())
            }
            return appList
        }

        fun getApps(context: Context): ApplicationList {
            val pm = context.packageManager
            val applications = pm.getInstalledApplications(GET_UNINSTALLED_PACKAGES)
            val systemApps = ArrayList<AppInfo>()
            val userApps = ArrayList<AppInfo>()
            for (app in applications) {
                val isSystem = (app.flags and ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM
                val isSDCard = ((app.flags and ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE)
                val file = File(app.sourceDir)
                val appSize = file.length()
                val appInfo = AppInfo.Builder()
                        .addName(app.loadLabel(pm).toString())
                        .addDrawable(app.loadIcon(pm))
                        .addPackageName(app.packageName)
                        .addSize(appSize)
                        .addIsSD(isSDCard)
                        .addIsSystem(isSystem)
                        .build()
                if (isSystem) {
                    systemApps.add(appInfo)
                } else {
                    userApps.add(appInfo)
                }
            }
            return ApplicationList(systemApps, userApps)
        }
    }


    class AppInfo {
        var name: String? = null
        var packageName: String? = null
        var icon: Drawable? = null
        var isSD: Boolean? = true
        var isSystem: Boolean = false
        var size: Long = 0

        class Builder {
            var name: String? = null
            var packageName: String? = null
            var icon: Drawable? = null
            var isSD: Boolean? = true
            var isSystem: Boolean = false
            var size: Long = 0

            fun addName(name: String): Builder {
                this.name = name
                return this
            }

            fun addPackageName(packageName: String): Builder {
                this.packageName = packageName
                return this
            }

            fun addDrawable(icon: Drawable): Builder {
                this.icon = icon
                return this
            }

            fun addIsSD(result: Boolean): Builder {
                this.isSD = result
                return this
            }

            fun addIsSystem(result: Boolean): Builder {
                this.isSystem = result
                return this
            }

            fun addSize(size: Long): Builder {
                this.size = size
                return this
            }

            fun build(): AppInfo {
                val appInfo = AppInfo()
                appInfo.name = this.name
                appInfo.packageName = this.packageName
                appInfo.icon = this.icon
                appInfo.isSD = this.isSD
                appInfo.isSystem = this.isSystem
                appInfo.size = this.size
                return appInfo
            }
        }

        override fun toString(): String {
            return "AppInfo(name=$name, packageName=$packageName, icon=$icon, isSD=$isSD, isSystem=$isSystem, size=$size)\n"
        }
    }

    class ApplicationList(var systemApps: List<AppsUtils.AppInfo>
                          , var userApps: List<AppsUtils.AppInfo>)
}

