package com.guard.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.ui.activities
 * @description: description
 * @date: 2018/5/29
 * @time: 20:06
 */
abstract class BaseSetUpActivity : AppCompatActivity() {
    val TAG: String = this.javaClass.simpleName
    lateinit var mGestureDetector: GestureDetector
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContextView())
        initGesture()
    }

    private fun initGesture() {
        mGestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            var startX: Float = 0.0f
            var endX: Float = 0.0f
            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                Log.d(TAG, "e1:${e1?.rawX},e2:${e2?.rawX},velocityx:$velocityX,velocity:$velocityY")
                startX = (e1?.rawX ?: 0) as Float
                endX = (e2?.rawX ?: 0) as Float
                if (endX - startX > 200 || velocityX > 200 && hasPrevious()) {
                    //previous - left
                    turnPreviousActivity()

                } else if (endX - startX < -200 || velocityY < -200 && hasNext()) {
                    // next -> right
                    turnNextActivity()
                }
                return super.onFling(e1, e2, velocityX, velocityY)
            }
        })
    }

    abstract fun turnPreviousActivity()
    abstract fun turnNextActivity()

    abstract fun getContextView(): Int

    abstract fun hasNext(): Boolean
    abstract fun hasPrevious(): Boolean

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mGestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    open fun next(view: View) {
        turnNextActivity()
    }

    fun pre(view: View) {
        turnPreviousActivity()
    }

}