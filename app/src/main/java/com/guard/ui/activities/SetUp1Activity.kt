package com.guard.ui.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.view.MotionEvent
import android.widget.Toast
import com.guard.R

class SetUp1Activity : BaseSetUpActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_up1)
        requestSmsPermission()
    }

    private fun requestSmsPermission() {
        var permission = Manifest.permission.READ_SMS
        var grant = ContextCompat.checkSelfPermission(this, permission)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(permission)
            ActivityCompat.requestPermissions(this, permissions, 1)
        }
        permission = Manifest.permission.ACCESS_COARSE_LOCATION
        grant = ContextCompat.checkSelfPermission(this, permission)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(permission)
            ActivityCompat.requestPermissions(this, permissions, 2)
        }

        permission = Manifest.permission.ACCESS_FINE_LOCATION
        grant = ContextCompat.checkSelfPermission(this, permission)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(permission)
            ActivityCompat.requestPermissions(this, permissions, 3)
        }
        permission = Manifest.permission.RECEIVE_SMS
        grant = ContextCompat.checkSelfPermission(this, permission)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(permission)
            ActivityCompat.requestPermissions(this, permissions, 4)
        }
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@SetUp1Activity, "access sms permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@SetUp1Activity, "access sms permission not granted", Toast.LENGTH_SHORT).show()
                }
            }
            2 -> {
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this@SetUp1Activity, "access location permission granted", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this@SetUp1Activity, "access location permission not granted", Toast.LENGTH_SHORT).show()
//
//                }
            }
            3 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@SetUp1Activity, "access fine location permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@SetUp1Activity, "access fine location permission not granted", Toast.LENGTH_SHORT).show()

                }
            }
            4 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@SetUp1Activity, "receive_sms permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@SetUp1Activity, "receive_sms permission not granted", Toast.LENGTH_SHORT).show()

                }
            }

        }
    }
}
