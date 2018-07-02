package com.guard.model.services

import android.app.PendingIntent
import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder
import android.text.format.Formatter
import android.widget.RemoteViews
import com.guard.R
import com.guard.model.receivers.WidgetReceiver
import com.guard.model.utils.ProcessUtil
import com.guard.model.utils.StorageUtil
import com.guard.ui.activities.ProcessManagerActivity
import java.util.*

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.model.services
 * @description: description
 * @date: 2018/6/28
 * @time: 16:34
 */
class WidgetService : Service() {
    private lateinit var appWidgetManager: AppWidgetManager
    private var timer: Timer? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        appWidgetManager = AppWidgetManager.getInstance(this) //获取widget的管理者
        updateMsg()
    }

    private fun updateMsg() {
        //时时刻刻更新Widget的数据,每隔一段时间更新一次数据，定时器，handler while(true) timer alarManager 线程池
        timer = Timer()
        //参数1:执行的任务
        //参数2:第一次执行的延迟时间
        //参数3:间隔时间
        timer?.schedule(object : TimerTask() {
            override fun run() {
                val componentName = ComponentName(this@WidgetService, WidgetReceiver::class.java)
                //layoutId:远程布局文件的id
                val remoteViews = RemoteViews(packageName, R.layout.process_widget)
                //更新widget中的内容,远程布局无法findViewById
                //更新相应控件的数据
                //参数1:更新数据的控件的id
                //参数2:更新的内容
                remoteViews.setTextViewText(R.id.process_count, "正在运行软件:${ProcessUtil.getRunningProcess(this@WidgetService).size}")
                val memoryInfo = StorageUtil.getMemoryInfo(this@WidgetService)
                remoteViews.setTextViewText(R.id.process_memory, "可用内存:${Formatter.formatFileSize(this@WidgetService, memoryInfo.memoryAvail)}")


                val intent = Intent(this@WidgetService, ProcessManagerActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(this@WidgetService, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)

                //setOnClickPendingIntent和setOnClickPendingIntent的区别是??
                //对于单个View来说是可以的，但是对于ListView这种多个条目的view来说,性能开销的代价非常大
                remoteViews.setOnClickPendingIntent(R.id.widget_ll_root,pendingIntent)
                appWidgetManager.updateAppWidget(componentName, remoteViews)
            }
        }, 0, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()    //取消timer任务
        timer = null
    }
}