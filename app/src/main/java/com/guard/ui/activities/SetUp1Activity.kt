package com.guard.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.guard.R

class SetUp1Activity : AppCompatActivity() {

    val TAG: String = this.javaClass.simpleName
    lateinit var mGestureDetector: GestureDetector
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_set_up1)
        mGestureDetector = GestureDetector(this, object : SimpleOnGestureListener() {
            var startX: Float = 0.0f
            var startY: Float = 0.0f
            var endX: Float = 0.0f
            var endY: Float = 0.0f
            override fun onDown(e: MotionEvent?): Boolean {
                startX = e?.rawX ?: 0.0f
                startY = e?.rawX ?: 0.0f
                return super.onDown(e)
            }

            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                Log.e(TAG, "distanceX:$distanceX")
                if (distanceX > 50) {
                    Toast.makeText(this@SetUp1Activity, "next onClick", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SetUp1Activity, SetUp2Activity::class.java))
                    overridePendingTransition(R.anim.setup_next_enter,
                            R.anim.setup_next_exit)
                    finish()
                }
                return super.onScroll(e1, e2, distanceX, distanceY)
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                Log.d(TAG, "e1:${e1?.rawX},e2:${e2?.rawX},velocityx:$velocityX,velocity:$velocityY")
                return super.onFling(e1, e2, velocityX, velocityY)
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mGestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    fun next(v: View?) {
        Toast.makeText(this, "next onClick", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, SetUp2Activity::class.java))
        overridePendingTransition(R.anim.setup_next_enter,
                R.anim.setup_next_exit)
        finish()
    }

}
