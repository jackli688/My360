package com.guard.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import com.guard.App
import com.guard.R
import com.guard.model.utils.AppsUtils
import com.guard.model.utils.StorageUtil
import com.guard.model.utils.UIUtils
import com.guard.persenter.SoftManagerPresenterImp
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
    private var softManagerPersenter: SoftManagerPresenterImp? = null
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
        processManager_rv_apps.apply {
            setHasFixedSize(true) //RecyclerView的一项优化
            layoutManager = viewManager
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    val linearLayoutManager = viewManager as LinearLayoutManager
                    val firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition()
                    softManagerPersenter?.judgeTitleWhatShow(firstVisiblePosition)
                    hideItemPopuWindow()
                    softManagerPersenter?.notifyScrollState(true)
                }
            })
            addOnItemTouchListener(ItemClickListener(processManager_rv_apps, object : SimpleClickListener() {
                override fun onItemClick(v: View, position: Int) {
                    softManagerPersenter?.judgeShowPopupWindow(v, position)
                }

            }))
        }
    }

    private fun initData() {
        softManagerPersenter = SoftManagerPresenterImp(this)
        softManagerPersenter?.loadData()
    }

    fun notifyDataSetChanged(result: AppsUtils.ApplicationList) {
        val softManagerAdapter = SoftManagerAdapter(this, result)
        processManager_rv_apps.apply { adapter = softManagerAdapter }
    }

    fun showMemoryMsg(memoryInfo: StorageUtil.MemoryInfo) {
        appManager_cp_memory.setText("内存")
        val usedMemory = memoryInfo.memoryCount - memoryInfo.memoryAvail
        val usedMemorytext = Formatter.formatFileSize(this, usedMemory)
        appManager_cp_memory.setUsedMemory(usedMemorytext)
        val availableMemorytext = Formatter.formatFileSize(this, memoryInfo.memoryAvail)
        appManager_cp_memory.setAvaliableMemory(availableMemorytext)
        appManager_cp_memory.setProgress(memoryInfo.usedMemoryRate)
    }


    fun showSDStorageMsg(sdCardInfo: StorageUtil.ExternalStorgeInfo) {
        processManager_cp_sd.setText("SD")
        val usedSD = sdCardInfo.totalSize - sdCardInfo.availSize
        val usedSDtext = Formatter.formatFileSize(this, usedSD)
        processManager_cp_sd.setUsedMemory(usedSDtext)
        val availableSDtext = Formatter.formatFileSize(this, sdCardInfo.availSize)
        processManager_cp_sd.setAvaliableMemory(availableSDtext)
        processManager_cp_sd.setProgress(sdCardInfo.getUsedRate())
    }

    override fun onDestroy() {
        softManagerPersenter?.onDetach()
        softManagerPersenter = null
        super.onDestroy()
    }

    fun showAppTitle(title: String) {
        processManager_tv_appTitle.text = title
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == UNISTALLAPK) {
            softManagerPersenter?.loadData()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun showItemPopupWindow(v: View, appInfo: AppsUtils.AppInfo) {
        if (popupWindow != null && popupWindow!!.isShowing) {
            popupWindow!!.dismiss()
        }
        val viewRoot = LayoutInflater.from(this).inflate(R.layout.softmanager_popupwindow, null, false)
        viewRoot.apply {
            findViewById(R.id.ll_uninstall).setOnClickListener {
                when {
                    packageName == appInfo.packageName -> {
                        toast("文明社会，拒绝自杀!!!")
                    }

                    appInfo.isSystem -> {
                        toast("系统应用，没有足够的权限进行卸载，，，，，，，，")
                    }

                    else -> {
                        val intent = Intent(Intent.ACTION_DELETE)
                        intent.addCategory(Intent.CATEGORY_DEFAULT)
                        val uri = Uri.fromParts("package", appInfo.packageName, null)
                        intent.data = uri
                        startActivityForResult(intent, UNISTALLAPK)
                    }
                }

            }
            findViewById(R.id.ll_open).setOnClickListener {
                val intent = packageManager.getLaunchIntentForPackage(appInfo.packageName)
                startActivity(intent)
            }
            findViewById(R.id.ll_share).setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, "发现一个好用的软件，${appInfo.name},想在尽请到应用宝")
                startActivity(intent)
            }
            findViewById(R.id.ll_info).setOnClickListener {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                val uri = Uri.fromParts("package", appInfo.packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }
        popupWindow = PopupWindow(viewRoot, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        popupWindow?.showAsDropDown(v, UIUtils.di2px(60.0f), -v.height)
    }

    fun hideItemPopuWindow() {
        if (popupWindow != null && popupWindow!!.isShowing) {
            popupWindow!!.dismiss()
            popupWindow = null
        }
    }

}

fun toast(str: String) = Toast.makeText(App.getContext(), str, Toast.LENGTH_SHORT).show()