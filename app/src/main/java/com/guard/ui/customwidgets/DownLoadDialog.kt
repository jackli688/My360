package com.guard.ui.customwidgets

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import com.guard.R
import com.guard.model.utils.UIUtils

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
        mDownloadmsg = findViewById(R.id.dialog_update_download_msg)
        mDownloadpb = findViewById(R.id.dialog_update_pb)
        mDownloadpb?.max = 100
        mDownloadpb?.progress = 50

    }


    fun setDownloadprogress(progress: Int) {
        mDownloadmsg?.text = "当前下载的进度是;" + progress.toString() + "%"
        mDownloadpb?.progress = progress
    }

    override fun show() {
        super.show()
        val window = this.window
        val attributes = window.attributes
        attributes.width = UIUtils.getScreenwidth()?.times(0.85f)!!.toInt()
        attributes.height = attributes.width.times(9 * 1.0f / 16).toInt()
        window.attributes = attributes
    }
}