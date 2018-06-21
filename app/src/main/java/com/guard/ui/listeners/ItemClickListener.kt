package com.guard.ui.listeners

import android.support.v4.view.GestureDetectorCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.ui
 * @description: description
 * @date: 2018/6/20
 * @time: 19:18
 */
class ItemClickListener(var hoster: RecyclerView, var l: OnItemClickListener) : RecyclerView.SimpleOnItemTouchListener() {
    private var gesture: GestureDetectorCompat = GestureDetectorCompat(hoster.context, ItemGestureListener())

    interface OnItemClickListener {
        fun onItemClick(v: View, position: Int)
        fun onItemLongClick(v: View, position: Int)
    }

    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {

    }

    override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
        gesture.onTouchEvent(e)
        return super.onInterceptTouchEvent(rv, e)
    }

    inner class ItemGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onLongPress(e: MotionEvent?) {
            val childView = hoster.findChildViewUnder(e?.x ?: 0.0f, e?.y ?: 0.0f)
            if (childView != null) {
                l.onItemLongClick(childView, hoster.getChildAdapterPosition(childView))
            }
        }


        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            val childView = hoster.findChildViewUnder(e?.x ?: 0.0f, e?.y ?: 0.0f)
//            val result = gesture.onTouchEvent(e)
//            Log.d("lijiwei", "result:$result")
            if (childView != null) {
                l.onItemClick(childView, hoster.getChildAdapterPosition(childView))
            }
            return true
        }
    }


}