package com.guard.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.guard.R
import com.guard.model.bean.Constants
import com.guard.model.utils.SharePreferencesUtils

class SetUp2Activity : AppCompatActivity() {

    var lockIcon: ImageView? = null
    var isRelativeSim: Boolean? = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_up2)
        val mRelSim = findViewById<RelativeLayout>(R.id.setup2_rel_sim)
        lockIcon = findViewById(R.id.setup2_iv_lockicon)
        isRelativeSim = SharePreferencesUtils.getBoolean(Constants.SPFILEA, Constants.ISRELSIM, false)
        if (isRelativeSim as Boolean) {
            lockIcon?.setImageResource(R.drawable.lock)
        } else {
            lockIcon?.setImageResource(R.drawable.unlock)
        }
        mRelSim.setOnClickListener {
            isRelativeSim = !isRelativeSim!!
            if (isRelativeSim as Boolean) {
                lockIcon?.setImageResource(R.drawable.lock)
            } else {
                lockIcon?.setImageResource(R.drawable.unlock)
            }
            isRelativeSim?.let { SharePreferencesUtils.setBoolean(Constants.SPFILEA, Constants.ISRELSIM, it) }
        }
    }

    fun next(view: View) {
        if (isRelativeSim!!) {
            startActivity(Intent(this, SetUp3Activity::class.java))
            overridePendingTransition(R.anim.setup_next_enter,
                    R.anim.setup_next_exit)
            finish()
        } else {
            Toast.makeText(this, "请先绑定SIM卡", Toast.LENGTH_SHORT).show()
        }
    }

    fun pre(view: View) {
        startActivity(Intent(this, SetUp1Activity::class.java))
        overridePendingTransition(R.anim.setup_pre_enter,
                R.anim.setup_pre_exit)
        finish()
    }
}
