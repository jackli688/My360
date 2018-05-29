package com.guard.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import com.guard.R

class SetUp1Activity : BaseSetUpActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_up1)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mGestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun getContextView(): Int {
        return R.layout.activity_set_up1
    }

    override fun hasNext(): Boolean {
        return true
    }

    override fun hasPrevious(): Boolean {
        return false
    }

    override fun turnPreviousActivity() {

    }

    override fun turnNextActivity() {
        Toast.makeText(this, "next onClick", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, SetUp2Activity::class.java))
        overridePendingTransition(R.anim.setup_next_enter,
                R.anim.setup_next_exit)
        finish()
    }
}
