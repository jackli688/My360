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
class SoftManagerPresenterImp(hoster: SoftManagerActivity) : IBasePresenter.SoftManagerPresenter<SoftManagerActivity> {
    private var mHoster: SoftManagerActivity? = null
    private var smBean: SoftManagerBean? = null
    private var perPosition: Int = -1
    private var isScroller: Boolean = false

    init {
        onAttach(hoster)
    }

    override fun onAttach(host: SoftManagerActivity) {
        mHoster = WeakReference<SoftManagerActivity>(host).get()
    }

    override fun loadData() {
        val loadAsyTask = LoadAsyTask(this)
        loadAsyTask.execute()
    }


    override fun onDetach() {
        if (mHoster === null)
            mHoster = null
    }

    override fun judgeTitleWhatShow(position: Int) {
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

    override fun judgeShowPopupWindow(v: View, position: Int) {
        if (perPosition != position || isScroller) {
            val userApps = smBean!!.apps.userApps
            val systemApps = smBean!!.apps.systemApps
            when {
                position == 0 -> {
                    mHoster?.hideItemPopuWindow()
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
                    mHoster?.hideItemPopuWindow()
                }

            }
            perPosition = position
            isScroller = false
        }

    }


    fun notifyScrollState(result: Boolean) {
        isScroller = result
    }

    class LoadAsyTask(var persenter: SoftManagerPresenterImp) : AsyncTask<Void, Void, SoftManagerBean>() {
        override fun doInBackground(vararg params: Void?): SoftManagerBean {
            val sdCardInfo = StorageUtil.getExternalStorgeInfo()
            val memoryInfo = StorageUtil.getMemoryInfo(App.getContext())
            val apps = AppsUtils.getApps(App.getContext())
            val softManagerBean = SoftManagerBean(sdCardInfo, memoryInfo, apps)
            persenter.smBean = softManagerBean
            return softManagerBean
        }

        override fun onPostExecute(result: SoftManagerBean) {
            persenter.mHoster?.notifyDataSetChanged(result.apps)
            persenter.mHoster?.showMemoryMsg(result.memoryInfo)
            persenter.mHoster?.showSDStorageMsg(result.sdCardInfo)
        }
    }

    class SoftManagerBean(var sdCardInfo: StorageUtil.ExternalStorgeInfo, var memoryInfo: StorageUtil.MemoryInfo
                          , var apps: AppsUtils.ApplicationList)
}