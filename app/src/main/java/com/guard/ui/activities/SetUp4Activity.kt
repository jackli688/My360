package com.guard.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.guard.R

class SetUp4Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_up4)
    }


    fun next(view: View) {
        startActivity(Intent(this, SetUp5Activity::class.java))
        overridePendingTransition(R.anim.setup_next_enter,
                R.anim.setup_next_exit)
        finish()
    }

    fun pre(view: View) {
        startActivity(Intent(this, SetUp3Activity::class.java))
        overridePendingTransition(R.anim.setup_pre_enter,
                R.anim.setup_pre_exit)
        finish()
    }

}
