package com.guard.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.guard.R
import com.guard.model.bean.Constants
import com.guard.model.utils.SharePreferencesUtils
import com.guard.ui.customwidgets.SettingItemView

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        autoUpdate = findViewById(R.id.autoUpdate)
        harassmentInterception = findViewById(R.id.harassmentInterception)
        attributionStyle = findViewById(R.id.attributionStyle)
        attribution = findViewById(R.id.attribution)

        autoUpdate?.setOnClickListener(this)
        harassmentInterception?.setOnClickListener(this)
        attributionStyle?.setOnClickListener(this)
        attribution?.setOnClickListener(this)

        initData()
    }

    private fun initData() {
        var boolean = getState(autoUpdate?.id.toString())
        autoUpdate?.setToggle(boolean)
        boolean = getState(harassmentInterception?.id.toString())
        harassmentInterception?.setToggle(boolean)
        boolean = getState(attributionStyle?.id.toString())
        attributionStyle?.setToggle(boolean)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.autoUpdate -> updateItemView(autoUpdate, Constants.SETTING_AUTOUPDATE)
            R.id.harassmentInterception -> {
                updateItemView(harassmentInterception, Constants.SETTING_HARASSMENTINTERCEPTION)
            }
            R.id.attribution -> updateItemView(attributionStyle, Constants.SETTING_ATTRIBUTION)
            R.id.attributionStyle -> showPickColorDialog()
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

}
