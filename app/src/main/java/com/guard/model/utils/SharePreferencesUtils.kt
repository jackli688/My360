package com.guard.model.utils

import android.content.Context
import android.content.SharedPreferences
import com.guard.App

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.model.utils
 * @description: description
 * @date: 2018/5/11
 * @time: 20:34
 */
object SharePreferencesUtils {

    private var sp: SharedPreferences? = null

    fun setBoolean(fileName: String, key: String, value: Boolean) {
        if (sp == null)
            sp = App.getContext()?.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        sp?.edit()?.putBoolean(key, value)?.apply()
    }

    fun getBoolean(fileName: String, key: String, defValue: Boolean): Boolean {
        if (sp == null)
            sp = App.getContext()?.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        return sp?.getBoolean(key, defValue) ?: defValue
    }

    fun setString(fileName: String, key: String, value: String) {
        if (sp == null)
            sp = App.getContext()?.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        sp?.edit()?.putString(key, value)?.apply()
    }

    fun getString(fileName: String, key: String, defValue: String?): String? {
        if (sp == null)
            sp = App.getContext()?.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        return sp?.getString(key, defValue)
    }


    fun setInt(fileName: String, key: String, value: Int) {
        if (sp == null)
            sp = App.getContext()?.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        sp?.edit()?.putInt(key, value)?.apply()
    }

    fun getInt(fileName: String, key: String, defValue: Int): Int {
        if (sp == null)
            sp = App.getContext()?.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        return sp?.getInt(key, defValue) ?: defValue
    }

}