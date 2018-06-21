package com.guard.persenter

import android.os.AsyncTask
import android.view.View
import com.guard.App
import com.guard.model.utils.AppsUtils
import com.guard.model.utils.StorageUtil
import com.guard.ui.activities.SoftManagerActivity
import java.lang.ref.WeakReference

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.persenter
 * @description: description
 * @date: 2018/6/19
 * @time: 22:41
 */
class SoftManagerPersenter(hoster: SoftManagerActivity) {
    private var mHoster: SoftManagerActivity? = null
    private var smBean: SoftManagerBean? = null

    init {
        onAttach(hoster)
    }

    private fun onAttach(hoster: SoftManagerActivity) {
        mHoster = WeakReference<SoftManagerActivity>(hoster).get()
    }

    fun loadData() {
        val loadAsyTask = LoadAsyTask()
        loadAsyTask.execute()
    }


    fun onDetach(hoster: SoftManagerActivity) {
        if (mHoster !== null)
            mHoster = null
    }

    fun judgeTitleWhatShow(position: Int) {
        if (smBean != null) {
            val userApps = smBean!!.apps.userApps
            val systemApps = smBean!!.apps.systemApps
            if (userApps.isNotEmpty() && systemApps.isNotEmpty()) {
                if (position < userApps.size + 1) {
                    mHoster?.showAppTitle("用户程序(${userApps.size}个)")
                } else {
                    mHoster?.showAppTitle("系统程序(${systemApps.size}个)")
                }
            }
        }
    }

    fun judgeShowPopupWindow(v: View, position: Int) {
        val userApps = smBean!!.apps.userApps
        val systemApps = smBean!!.apps.systemApps
        when {
            position == 0 -> {
//                mHoster?.hidePopupWindow()
            }
            0 < position && position < userApps.size + 1 -> {
                val appInfo = userApps[position - 1]
                mHoster?.showItemPopupWindow(v, appInfo)
            }
            position == userApps.size + 1 -> {
            }
            userApps.size + 1 < position && position < userApps.size + systemApps.size + 2 -> {
                val appInfo = systemApps[position - userApps.size - 1]
                mHoster?.showItemPopupWindow(v, appInfo)
            }
            else -> {

            }

        }
    }

    inner class LoadAsyTask : AsyncTask<Void, Void, SoftManagerBean>() {
        override fun doInBackground(vararg params: Void?): SoftManagerBean {
            val sdCardInfo = StorageUtil.getExternalStorgeInfo()
            val memoryInfo = StorageUtil.getMemoryInfo(App.getContext())
            val apps = AppsUtils.getApps(App.getContext())
            val softManagerBean = SoftManagerBean(sdCardInfo, memoryInfo, apps)
            smBean = softManagerBean
            return softManagerBean
        }

        override fun onPostExecute(result: SoftManagerBean) {
            mHoster?.notifyDataSetChanged(result.apps)
            mHoster?.showMemoryMsg(result.memoryInfo)
            mHoster?.showSDStorageMsg(result.sdCardInfo)
        }
    }

    class SoftManagerBean(var sdCardInfo: StorageUtil.ExternalStorgeInfo, var memoryInfo: StorageUtil.MemoryInfo
                          , var apps: AppsUtils.ApplicationList)
}