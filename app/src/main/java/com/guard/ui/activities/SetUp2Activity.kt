package com.guard.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.guard.R
import com.guard.model.bean.Constants
import com.guard.model.utils.SharePreferencesUtils

class SetUp2Activity : AppCompatActivity() {

    var lockIcon: ImageView? = null
    var ownPermission: Boolean? = false
    val TAG: String = this.javaClass.simpleName
    lateinit var mGestureDetector: GestureDetector
    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_up2)
        ownPermission = if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), 1)
            false
        } else {
            true
        }
        val mRelSim = findViewById<RelativeLayout>(R.id.setup2_rel_sim)
        lockIcon = findViewById(R.id.setup2_iv_lockicon)
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
        mGestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
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
                    if (!TextUtils.isEmpty(SharePreferencesUtils.getString(Constants.SPFILEA, Constants.SIMSERIALNUMBER, null))) {
                        startActivity(Intent(this@SetUp2Activity, SetUp3Activity::class.java))
                        overridePendingTransition(R.anim.setup_next_enter,
                                R.anim.setup_next_exit)
                        finish()
                    } else {
                        Toast.makeText(this@SetUp2Activity, "请先绑定SIM卡", Toast.LENGTH_SHORT).show()
                    }
                } else if (distanceX < -50) {
                    startActivity(Intent(this@SetUp2Activity, SetUp1Activity::class.java))
                    overridePendingTransition(R.anim.setup_pre_enter,
                            R.anim.setup_pre_exit)
                    finish()
                }
                return super.onScroll(e1, e2, distanceX, distanceY)
            }
        })
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mGestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    fun next(view: View) {
        if (!TextUtils.isEmpty(SharePreferencesUtils.getString(Constants.SPFILEA, Constants.SIMSERIALNUMBER, null))) {
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


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> ownPermission = (permissions.isNotEmpty() && permissions[0] == Manifest.permission.READ_PHONE_STATE
                    && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        }
    }
}
