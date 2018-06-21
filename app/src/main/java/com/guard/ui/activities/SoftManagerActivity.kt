package com.guard.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import com.guard.R
import com.guard.model.utils.AppsUtils
import com.guard.model.utils.StorageUtil
import com.guard.model.utils.UIUtils
import com.guard.persenter.SoftManagerPersenter
import com.guard.ui.adapters.SoftManagerAdapter
import com.guard.ui.listeners.ItemClickListener
import com.guard.ui.listeners.SimpleClickListener
import kotlinx.android.synthetic.main.activity_softmanager.*

/**
 * chrome://extensions/
 * Android获取SD卡总容量，可用大小，机身内存总容量以及可用大小的系统方法
 * https://blog.csdn.net/xiaoqun999/article/details/68946468
 */

class SoftManagerActivity : AppCompatActivity() {
    private val Tag = SoftManagerActivity::class.java.simpleName
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var softManagerPersenter: SoftManagerPersenter? = null
    private var popupWindow: PopupWindow? = null

    companion object {
        private const val UNISTALLAPK = 10
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_softmanager)
        initView()
        initData()
    }

    private fun initView() {
        viewManager = LinearLayoutManager(this)
        appmanager_rv_apps.apply {
            setHasFixedSize(true) //RecyclerView的一项优化
            layoutManager = viewManager
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    val linearLayoutManager = viewManager as LinearLayoutManager
                    val firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition()
                    hideItemPopupWindow()
                    softManagerPersenter?.judgeTitleWhatShow(firstVisiblePosition)
                }
            })
            appmanager_rv_apps.addOnItemTouchListener(ItemClickListener(appmanager_rv_apps, object : SimpleClickListener() {
                override fun onItemClick(v: View, position: Int) {
                    softManagerPersenter?.judgeShowPopupWindow(v, position)
                }

            }))
        }
    }

    private fun initData() {
        softManagerPersenter = SoftManagerPersenter(this)
        softManagerPersenter?.loadData()
    }

    fun notifyDataSetChanged(result: AppsUtils.ApplicationList) {
        val softManagerAdapter = SoftManagerAdapter(this, result)
        appmanager_rv_apps.apply { adapter = softManagerAdapter }
    }

    fun showItemPopupWindow(v: View, app: AppsUtils.AppInfo) {
        hideItemPopupWindow()
        val showView = LayoutInflater.from(this).inflate(R.layout.softmanager_popupwindow, null, false)
        popupWindow = PopupWindow(showView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        popupWindow?.showAsDropDown(v, UIUtils.di2px(60.0f), -v.height)
        showView.findViewById(R.id.ll_uninstall).apply { setOnClickListener { unInstallApk(app) } }
        showView.findViewById(R.id.ll_open).apply { setOnClickListener { startApk(app.packageName) } }
        showView.findViewById(R.id.ll_share).apply { setOnClickListener { shareApk(app) } }
        showView.findViewById(R.id.ll_info).apply { setOnClickListener { showApkInfo(app) } }
    }

    private fun showApkInfo(app: AppsUtils.AppInfo) {
        Uri.fromParts("package:", app.packageName, null)
        //Uri.parse的源码应该好好看看
        val uri = Uri.parse("package:${app.packageName}")
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)
        startActivity(intent)
    }

    private fun shareApk(app: AppsUtils.AppInfo) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "发现了一个很好用的软件:${app.name},在应用宝中可以找到！！！！")
        startActivity(intent)
    }

    private fun startApk(packageName: String?) {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "native程序,这里无法启动", Toast.LENGTH_SHORT).show()
        }
    }

    private fun unInstallApk(app: AppsUtils.AppInfo) {
        if (packageName == app.packageName) {
            Toast.makeText(this, "文明社会,杜绝自杀....", Toast.LENGTH_SHORT).show()
        } else {
            if (!TextUtils.isEmpty(app.packageName)) {
                if (!app.isSystem!!) {
                    val uri = Uri.parse("package:${app.packageName}")
                    val intent = Intent(Intent.ACTION_DELETE, uri)
                    startActivityForResult(intent, UNISTALLAPK)
                } else {
                    Toast.makeText(this, "系统应用，请授予Root权限", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun hideItemPopupWindow() {
        popupWindow?.dismiss()
        popupWindow = null
    }


    fun showMemoryMsg(memoryInfo: StorageUtil.MemoryInfo) {
        appmanager_cp_memory.setText("内存")
        val usedMemory = memoryInfo.memoryCount - memoryInfo.memoryAvail
        val usedMemorytext = Formatter.formatFileSize(this, usedMemory)
        appmanager_cp_memory.setUsedMemory(usedMemorytext)
        val availableMemorytext = Formatter.formatFileSize(this, memoryInfo.memoryAvail)
        appmanager_cp_memory.setAvaliableMemory(availableMemorytext)
        appmanager_cp_memory.setProgress(memoryInfo.getUsedRate())
    }


    fun showSDStorageMsg(sdCardInfo: StorageUtil.ExternalStorgeInfo) {
        appmanager_cp_sd.setText("SD")
        val usedSD = sdCardInfo.totalSize - sdCardInfo.availSize
        val usedSDtext = Formatter.formatFileSize(this, usedSD)
        appmanager_cp_sd.setUsedMemory(usedSDtext)
        val availableSDtext = Formatter.formatFileSize(this, sdCardInfo.availSize)
        appmanager_cp_sd.setAvaliableMemory(availableSDtext)
        appmanager_cp_sd.setProgress(sdCardInfo.getUsedRate())
    }

    override fun onDestroy() {
        softManagerPersenter = null
        hideItemPopupWindow()
        super.onDestroy()
    }

    fun showAppTitle(title: String) {
        appmanager_tv_userAppstitle.text = title
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == UNISTALLAPK) {
            softManagerPersenter?.loadData()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
