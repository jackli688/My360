package com.guard.ui.customview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.ui.customview
 * @description: description
 * @date: 2018/6/10
 * @time: 11:42
 * https://blog.csdn.net/bigconvience/article/details/26697645https://blog.csdn.net/bigconvience/article/details/26697645
 */
class MoveView : View {

    var startX: Int = 0
    var startY: Int = 0
    var scroller: Scroller? = null
    var Tag = this.javaClass.simpleName

    internal constructor(context: Context?) : this(context, null)

    internal constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)


    internal constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        scroller = Scroller(context)
    }

//    override fun computeScroll() {
//        if (scroller!!.computeScrollOffset()) {
//            val view = parent as View
//            view.scrollTo(scroller!!.currX, scroller!!.currY)
//            invalidate()
//        }
//    }
//
//
//    fun smoothScrollTo(destX: Int, destY: Int) {
//        val scrollX = this.scrollX
//        val delta = destX - scrollX
//        Log.d("moveview", "scrollX:$scrollX--------------delta:$delta")
//        scroller?.startScroll(scrollX, 0, delta, 0, 2000)
//        invalidate()
//    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d(Tag, "action press down")
                    startX = event.x.toInt()
                    startY = event.y.toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    Log.d(Tag, "action press move")
                    val offsetX = event.x.toInt() - this.startX
                    val offsetY = event.y.toInt() - this.startY
                    layout(left + offsetX, top + offsetY, right + offsetX, bottom + offsetY)
                    startX = event.x.toInt()
                    startY = event.y.toInt()
                }
                MotionEvent.ACTION_UP -> {
                    Log.d(Tag, "action press up")

                }
            }
        }
        return super.onTouchEvent(event)
    }


}