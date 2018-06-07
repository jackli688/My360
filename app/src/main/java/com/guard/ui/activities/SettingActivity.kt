package com.guard.ui.activities

import android.Manifest
import android.Manifest.permission.SYSTEM_ALERT_WINDOW
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.net.http.SslCertificate.saveState
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.guard.R
import com.guard.model.bean.Constants
import com.guard.model.services.AddressService
import com.guard.model.services.BlackNumberService
import com.guard.model.utils.ServiceUtils
import com.guard.model.utils.SharePreferencesUtils
import com.guard.ui.customwidgets.SettingItemView

@Suppress("DEPRECATION")
/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.ui.activities
 * @description: description
 * @date: 2018/5/9
 * @time: 11:47
 */
class SettingActivity : AppCompatActivity(), View.OnClickListener {

    var autoUpdate: SettingItemView? = null
    var harassmentInterception: SettingItemView? = null
    var attributionStyle: SettingItemView? = null
    var attribution: SettingItemView? = null
    val REQUESTCALLCODE = 1
    val REQUESTSYSTEM_ALERT_WINDOWCODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        autoUpdate = findViewById(R.id.autoUpdate)
        harassmentInterception = findViewById(R.id.harassmentInterception)
        attributionStyle = findViewById(R.id.attributionStyle)
        attribution = findViewById(R.id.attribution)

        autoUpdate?.setOnClickListener(this)
        harassmentInterception?.setOnClickListener(this)
        attribution?.setOnClickListener(this)
        attributionStyle?.setOnClickListener(this)

        initData()
    }


    override fun onStart() {
        super.onStart()
        if (ServiceUtils.checkServiceIsRunning(this, BlackNumberService::class.java.name)) {
            harassmentInterception?.setToggle(true)
        } else {
            harassmentInterception?.setToggle(false)
        }

        if (ServiceUtils.checkServiceIsRunning(this, AddressService::class.java.name)) {
            attribution?.setToggle(true)
        } else {
            attribution?.setToggle(false)
        }
    }

    private fun initData() {
        var boolean = getState(Constants.SETTING_AUTOUPDATE)
        autoUpdate?.setToggle(boolean)
        boolean = getState(Constants.SETTING_HARASSMENTINTERCEPTION)
        harassmentInterception?.setToggle(boolean)
        boolean = getState(Constants.SETTING_ATTRIBUTION)
        attributionStyle?.setToggle(boolean)
    }

    @SuppressLint("ServiceCast")
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.autoUpdate -> {
                updateItemView(autoUpdate, Constants.SETTING_AUTOUPDATE)
            }
            R.id.harassmentInterception -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    val permission = "android.Manifest.permission.READ_PHONE_STATE"
                    val permission = Manifest.permission.PROCESS_OUTGOING_CALLS
                    val checkResult = ContextCompat.checkSelfPermission(this, permission)
                    if (checkResult != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this@SettingActivity, permission)) {
                            //拒绝后,再次申请的时候，可以添加一些描述性为甚么要这个权限，用来做什么
                        } else {
                            requestPermissions(arrayOf(permission), REQUESTCALLCODE)
                        }
                    } else {
                        turnInterceptionService()
                    }
                } else {
                    //开启服务->关闭服务-> 关闭服务-> 开启服务
                    turnInterceptionService()
                }
            }
            R.id.attribution -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(this)) {
                        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                        intent.data = Uri.parse("package:$packageName")
                        startActivityForResult(intent, 100)
                    } else {
                        //6.0系统
                        val intent = Intent(this@SettingActivity, AddressService::class.java)
                        if (ServiceUtils.checkServiceIsRunning(this, AddressService::class.java.name)) {
                            stopService(intent)
                            attribution?.setToggle(false)
                        } else {
                            startService(intent)
                            attribution?.setToggle(true)
                        }
                    }
                } else {
                    //6.0系统
                    val intent = Intent(this@SettingActivity, AddressService::class.java)
                    if (ServiceUtils.checkServiceIsRunning(this, AddressService::class.java.name)) {
                        stopService(intent)
                        attribution?.setToggle(false)
                    } else {
                        startService(intent)
                        attribution?.setToggle(true)
                    }
                }

            }

            R.id.attributionStyle -> showPickColorDialog()
        }
    }

    private fun turnInterceptionService() {
        val intent = Intent(this@SettingActivity, BlackNumberService::class.java)
        if (ServiceUtils.checkServiceIsRunning(this, BlackNumberService::class.java.name)) {
            stopService(intent)
            harassmentInterception?.setToggle(false)
        } else {
            startService(intent)
            harassmentInterception?.setToggle(true)
        }
    }

    private fun showPickColorDialog() {

    }

    private fun updateItemView(settingItemView: SettingItemView?, key: String) {
        var b = settingItemView?.getToggle()
        b = !(b ?: true)
        settingItemView?.setToggle(b)
        saveState(key, b)
    }


    private fun getState(key: String): Boolean {
        return SharePreferencesUtils.getBoolean(Constants.SPFILEA, key, false)
    }

    private fun saveState(key: String, value: Boolean) {
        SharePreferencesUtils.setBoolean(Constants.SPFILEA, key, value)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUESTCALLCODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@SettingActivity, "打电话权限已经给了...", Toast.LENGTH_SHORT).show()
                    turnInterceptionService()
                } else {
                    Toast.makeText(this@SettingActivity, "打电话权限没有授权...", Toast.LENGTH_SHORT).show()
                }
                return

            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permission = SYSTEM_ALERT_WINDOW
                val result = ContextCompat.checkSelfPermission(this, permission)
                if (result == PackageManager.PERMISSION_GRANTED) {
                    //6.0系统
                    val intent = Intent(this@SettingActivity, AddressService::class.java)
                    if (ServiceUtils.checkServiceIsRunning(this, AddressService::class.java.name)) {
                        stopService(intent)
                        attribution?.setToggle(false)
                    } else {
                        startService(intent)
                        attribution?.setToggle(true)
                    }
                } else {
                    Toast.makeText(this, "悬浮窗的权限没有给叔,来电提醒功能将不能正常使用", Toast.LENGTH_SHORT).show()
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}

