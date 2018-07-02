package com.guard.model.receivers

import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import com.guard.model.services.WidgetService

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.ui.customwidgets
 * @description: description
 * https://juejin.im/post/5a5802426fb9a01c9950bbad#heading-5
 * @date: 2018/6/28
 * @time: 16:18
 */
class WidgetReceiver : AppWidgetProvider() {

    override fun onEnabled(context: Context?) {
//        super.onEnabled(context)
        val intent = Intent(context, WidgetReceiver::class.java)
        context?.startService(intent)
    }


    override fun onDisabled(context: Context?) {
//        super.onDisabled(context)
        val intent = Intent(context, WidgetService::class.java)
        context?.stopService(intent)
    }

}