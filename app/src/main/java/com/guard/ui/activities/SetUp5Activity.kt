package com.guard.ui.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.CheckBox
import android.widget.Toast
import com.guard.R
import com.guard.model.bean.Constants
import com.guard.model.utils.SharePreferencesUtils

class SetUp5Activity : BaseSetUpActivity() {

    lateinit var mProtected: CheckBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_up5)
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BROADCAST_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(android.Manifest.permission.BROADCAST_SMS), 1)
            }
        }
        mProtected = findViewById(R.id.setup5_cb_protected) as CheckBox
        mProtected.isChecked = SharePreferencesUtils.getBoolean(Constants.SPFILEA, Constants.LOSTFIND_REL_PROTECTED, false)
        mProtected.setOnClickListener {
            val result = SharePreferencesUtils.getBoolean(Constants.SPFILEA, Constants.LOSTFIND_REL_PROTECTED, false)
            SharePreferencesUtils.setBoolean(Constants.SPFILEA, Constants.LOSTFIND_REL_PROTECTED, !result)
            mProtected.isChecked = !result
        }
    }

    override fun turnPreviousActivity() {
        startActivity(Intent(this, SetUp4Activity::class.java))
        overridePendingTransition(R.anim.setup_pre_enter,
                R.anim.setup_pre_exit)
        finish()
    }

    override fun turnNextActivity() {
        if (SharePreferencesUtils.getBoolean(Constants.SPFILEA, Constants.LOSTFIND_REL_PROTECTED, false)) {
            val intent = Intent(this, LostFindActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this@SetUp5Activity, "请勾选手机丢失保护", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getContextView(): Int {
        return R.layout.activity_set_up5
    }

    override fun hasNext(): Boolean {
        return true
    }

    override fun hasPrevious(): Boolean {
        return true
    }
}
