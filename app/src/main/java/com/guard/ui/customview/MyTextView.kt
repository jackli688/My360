package com.guard.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.ui.customview
 * @description: description
 * @date: 2018/6/7
 * @time: 12:06
 */
class MyTextView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : TextView(context, attrs, defStyleAttr), View.OnTouchListener {

    constructor(context: Context?) : this(context, null, 0)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {

            }
            MotionEvent.ACTION_MOVE -> {

            }
            MotionEvent.ACTION_UP -> {

            }
            else -> {
                return false
            }
        }
        return false
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {

            }
            MotionEvent.ACTION_MOVE -> {

            }
            MotionEvent.ACTION_UP -> {

            }
        }
        return super.onTouchEvent(event)
    }

}