package com.guard.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.guard.R
import com.guard.model.bean.Constants
import com.guard.model.utils.SharePreferencesUtils

class SetUp2Activity : BaseSetUpActivity() {

    var lockIcon: ImageView? = null
    var ownPermission: Boolean? = false
    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ownPermission = if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), 1)
            false
        } else {
            true
        }
        val mRelSim = findViewById(R.id.setup2_rel_sim) as RelativeLayout
        lockIcon = findViewById(R.id.setup2_iv_lockicon) as ImageView
        var sp_sim = SharePreferencesUtils.getString(Constants.SPFILEA, Constants.SIMSERIALNUMBER, null)
        if (TextUtils.isEmpty(sp_sim)) {
            lockIcon?.setImageResource(R.drawable.unlock)
        } else {
            lockIcon?.setImageResource(R.drawable.lock)
        }
        mRelSim.setOnClickListener {
            if (ownPermission as Boolean) {
                sp_sim = SharePreferencesUtils.getString(Constants.SPFILEA, Constants.SIMSERIALNUMBER, null)
                if (TextUtils.isEmpty(sp_sim)) {
                    var tel = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    sp_sim = tel.simSerialNumber
                    SharePreferencesUtils.setString(Constants.SPFILEA, Constants.SIMSERIALNUMBER, sp_sim!!)
                    lockIcon?.setImageResource(R.drawable.lock)
                } else {
                    SharePreferencesUtils.setString(Constants.SPFILEA, Constants.SIMSERIALNUMBER, "")
                    lockIcon?.setImageResource(R.drawable.unlock)
                }
            } else {
                Toast.makeText(this, "请授予读取SIM卡权限", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> ownPermission = (permissions.isNotEmpty() && permissions[0] == Manifest.permission.READ_PHONE_STATE
                    && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        }
    }

    override fun turnPreviousActivity() {
        startActivity(Intent(this, SetUp1Activity::class.java))
        overridePendingTransition(R.anim.setup_pre_enter,
                R.anim.setup_pre_exit)
        finish()
    }

    override fun turnNextActivity() {
        if (!TextUtils.isEmpty(SharePreferencesUtils.getString(Constants.SPFILEA, Constants.SIMSERIALNUMBER, null))) {
            startActivity(Intent(this, SetUp3Activity::class.java))
            overridePendingTransition(R.anim.setup_next_enter,
                    R.anim.setup_next_exit)
            finish()
        } else {
            Toast.makeText(this, "请先绑定SIM卡", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getContextView(): Int {
        return R.layout.activity_set_up2
    }

    override fun hasNext(): Boolean {
        return true
    }

    override fun hasPrevious(): Boolean {
        return true
    }
}
