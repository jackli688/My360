package com.guard.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.guard.R

class SetUp1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_up1)
    }

    fun next(v: View?) {
        Toast.makeText(this, "next onClick", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, SetUp2Activity::class.java))
        overridePendingTransition(R.anim.setup_next_enter,
                R.anim.setup_next_exit)
        finish()
    }

}
