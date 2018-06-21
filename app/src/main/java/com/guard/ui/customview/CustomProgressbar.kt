package com.guard.ui.customview

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ProgressBar
import android.widget.TextView
import com.guard.R

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.ui.customview
 * @description: description
 * @date: 2018/6/18
 * @time: 16:45
 */
class CustomProgressbar(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : ConstraintLayout(context, attrs, defStyleAttr) {

    var mText: TextView
    var mProgressbar: ProgressBar
    var mLeft: TextView
    var mRight: TextView

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?) : this(context, null)

    init {
        val rootView = LayoutInflater.from(context).inflate(R.layout.custom_progressbar, null)
        this.addView(rootView)
        mText = rootView.findViewById(R.id.memory) as TextView
        mProgressbar = rootView.findViewById(R.id.appmanager_pb_progressbar) as ProgressBar
        mLeft = rootView.findViewById(R.id.appmanager_tv_left) as TextView
        mRight = rootView.findViewById(R.id.appmanager_tv_right) as TextView
    }


    fun setText(text: String) {
        mText.text = text
    }


    fun setUsedMemory(text: String) {
        mLeft.text = text
    }

    fun setAvaliableMemory(text: String) {
        mRight.text = text
    }

    fun setProgress(progress: Int) {
        mProgressbar.progress = progress
    }

}