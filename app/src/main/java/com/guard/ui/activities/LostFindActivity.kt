package com.guard.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.guard.R
import com.guard.model.bean.Constants
import com.guard.model.utils.SharePreferencesUtils

class LostFindActivity : AppCompatActivity() {

    lateinit var mSafeNumber: TextView
    lateinit var mLostfind: ImageView
    lateinit var mTryAgain: TextView
    lateinit var mRelProtected: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharePreferencesUtils.setBoolean(Constants.SPFILEA, Constants.ISFIRSTENTERANTITHEFTACTIVITY, false)
        setContentView(R.layout.activity_lost_find)
        mSafeNumber = findViewById(R.id.lostfind_tv_safenumber) as TextView
        mLostfind = findViewById(R.id.lostfind_tv_icon) as ImageView
        mRelProtected = findViewById(R.id.lostfind_rel_protected) as RelativeLayout
        mTryAgain = findViewById(R.id.lostfind_tv_agin) as TextView
        val safeNumber = SharePreferencesUtils.getString(Constants.SPFILEA, Constants.SAFENUMBER, null)
        mSafeNumber.text = safeNumber
        val relProtected = SharePreferencesUtils.getBoolean(Constants.SPFILEA, Constants.LOSTFIND_REL_PROTECTED, false)
        if (relProtected) {
            mLostfind.setImageResource(R.drawable.lock)
        } else {
            mLostfind.setImageResource(R.drawable.unlock)
        }
        mRelProtected.setOnClickListener {
            val result = SharePreferencesUtils.getBoolean(Constants.SPFILEA, Constants.LOSTFIND_REL_PROTECTED, false)
            if (result) {
                mLostfind.setImageResource(R.drawable.unlock)
            } else {
                mLostfind.setImageResource(R.drawable.lock)
            }
            SharePreferencesUtils.setBoolean(Constants.SPFILEA, Constants.LOSTFIND_REL_PROTECTED, !result)
        }

        mTryAgain.setOnClickListener {
            startActivity(Intent(this@LostFindActivity, SetUp1Activity::class.java))
            finish()
        }

    }
}
