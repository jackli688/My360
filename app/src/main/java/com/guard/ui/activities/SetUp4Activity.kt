package com.guard.ui.activities

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.guard.R
import com.guard.model.receivers.AdminReceiver

class SetUp4Activity : AppCompatActivity() {

    private val REQUEST_CODE_ENABLE_ADMIN = 100
    lateinit var mRelAdmin: RelativeLayout
    lateinit var mIcon: ImageView
    lateinit var mDevicePolicyManager: DevicePolicyManager
    lateinit var mDeviceComponentName: ComponentName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_up4)
        mRelAdmin = findViewById(R.id.setup4_rel_admin)
        mIcon = findViewById(R.id.setup4_iv_icon)
        mDevicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        mDeviceComponentName = ComponentName(this, AdminReceiver::class.java)

        if (mDevicePolicyManager.isAdminActive(mDeviceComponentName)) {
            mIcon.setImageResource(R.drawable.admin_activated)
        } else {
            mIcon.setImageResource(R.drawable.admin_inactivated)
        }
        mRelAdmin.setOnClickListener { v ->
            if (mDevicePolicyManager.isAdminActive(mDeviceComponentName)) {
                mDevicePolicyManager.removeActiveAdmin(mDeviceComponentName)
                mIcon.setImageResource(R.drawable.admin_inactivated)
            } else {
                //跳转到激活界面
                val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
                //激活哪一个设备管理器
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceComponentName)
                //设置描述信息
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "my360")
                startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ENABLE_ADMIN) {
            if (mDevicePolicyManager.isAdminActive(mDeviceComponentName)) {
                mIcon.setImageResource(R.drawable.admin_activated)
            } else {
                mIcon.setImageResource(R.drawable.admin_inactivated)
            }
        }
    }

    fun next(view: View) {
        if (mDevicePolicyManager.isAdminActive(mDeviceComponentName)) {
            startActivity(Intent(this, SetUp5Activity::class.java))
            overridePendingTransition(R.anim.setup_next_enter,
                    R.anim.setup_next_exit)
            finish()
        } else {
            Toast.makeText(this@SetUp4Activity, "请先激活设备管理员", Toast.LENGTH_SHORT).show()
        }
    }

    fun pre(view: View) {
        startActivity(Intent(this, SetUp3Activity::class.java))
        overridePendingTransition(R.anim.setup_pre_enter,
                R.anim.setup_pre_exit)
        finish()
    }

}
