package com.guard

import android.app.Activity
import android.app.Application
import android.content.Context

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard
 * @description: description
 * @date: 2018/5/7
 * @time: 22:41
 */
class App : Application() {

    companion object {
        private var mApp: App? = null
        fun getInstance(): App? {
            return mApp
        }

        fun getContext(context: Context): Context {
            return context.applicationContext
        }

        fun getContext(activity: Activity): Context {
            return activity.baseContext
        }

        fun getContext(): Context? {
            return getInstance()?.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        mApp = this
    }
}