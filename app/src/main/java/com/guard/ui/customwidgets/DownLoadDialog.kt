package com.guard.ui.customwidgets

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import com.guard.R

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.ui.customwidgets
 * @description: description
 * @date: 2018/5/6
 * @time: 16:11
 */
class DownLoadDialog(context: Context?) : Dialog(context) {

    var mDownloadmsg: TextView? = null
    var mDownloadpb: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.diloag_download)
        mDownloadmsg = findViewById(R.id.download_msg)
        mDownloadpb = findViewById(R.id.download_dialog_pb)
    }

    fun setDownloadprogress(progress: Int) {
        mDownloadmsg?.text = "当前下载的进度是;" + progress.toString() + "%"
        mDownloadpb?.progress = progress
    }
}