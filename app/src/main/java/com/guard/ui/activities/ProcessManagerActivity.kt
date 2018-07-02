package com.guard.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.format.Formatter
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.guard.R
import com.guard.model.bean.Constants
import com.guard.model.services.LockScreenService
import com.guard.model.utils.SharePreferencesUtils
import com.guard.model.utils.StorageUtil
import com.guard.persenter.ProcessManagerPresenterImp
import com.guard.persenter.ProcessManagerPresenterImp.ProcessBean
import com.guard.persenter.ProcessManagerPresenterImp.ProcessListBean
import com.guard.ui.adapters.ProcessManagerAdapter
import com.guard.ui.listeners.ItemClickListener
import com.guard.ui.listeners.SimpleClickListener
import kotlinx.android.synthetic.main.activity_processmanager.*

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.ui.activities
 * @description: description
 * @date: 2018/6/24
 * @time: 9:42
 */
class ProcessManagerActivity : AppCompatActivity() {

    private lateinit var mPresenterImp: ProcessManagerPresenterImp
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var mAdapter: ProcessManagerAdapter? = null
    var showSystem: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_processmanager)
        initView()
        initData()
    }

    private fun initData() {
        mPresenterImp = ProcessManagerPresenterImp(this)
        mPresenterImp.loadData()
    }

    private fun initView() {
        viewManager = LinearLayoutManager(this)
        processManager_rv_processList.apply {
            setHasFixedSize(true) //RecyclerView的一项优化
            layoutManager = viewManager
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    val linearLayoutManager = viewManager as LinearLayoutManager
                    val firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition()
                    mPresenterImp.judgeTitleWhatShow(firstVisiblePosition)
                }
            })
            addOnItemTouchListener(ItemClickListener(this, object : SimpleClickListener() {
                override fun onItemClick(v: View, position: Int) {
                    mPresenterImp.notifyItemSelected(position)
                }

            }))
        }


        processManager_bt_all.setOnClickListener {
            mPresenterImp.notifyItemAllSelected()
        }


        processManager_bt_none.setOnClickListener {
            mPresenterImp.notifyItemReverse()
        }

        processManager_iv_clear.setOnClickListener {
            mPresenterImp.killCheckedProcess()
        }

        setAnimation()//设置动画
        processManager_sd_drawer.setOnDrawerOpenListener {
            closeAnimation()
        }
        processManager_sd_drawer.setOnDrawerCloseListener {
            setAnimation()
        }
        processManager_sv_showSystem.setOnClickListener {
            processManager_sv_showSystem.toggle()
            showSystem = processManager_sv_showSystem.getToggle()
            mAdapter?.mShowSystem = showSystem
            SharePreferencesUtils.setBoolean(Constants.SPFILEA, Constants.SHOWSYSTEMPROCESS, showSystem)
            mAdapter?.notifyDataSetChanged()
        }


        processManager_sv_lockScreen.setOnClickListener {
            Intent(this@ProcessManagerActivity, LockScreenService::class.java)
        }
    }


    override fun onStart() {
        super.onStart()
        val result = SharePreferencesUtils.getBoolean(Constants.SPFILEA, Constants.SHOWSYSTEMPROCESS, false)
        showSystem = result
        mAdapter?.mShowSystem = showSystem
        mAdapter?.notifyDataSetChanged()
        processManager_sv_showSystem.setToggle(showSystem)
    }

    private fun closeAnimation() {
        processManager_iv_drawer1.clearAnimation()
        processManager_iv_drawer2.clearAnimation()

        //更改箭头方向
        processManager_iv_drawer1.setImageResource(R.drawable.drawer_arrow_down)
        processManager_iv_drawer2.setImageResource(R.drawable.drawer_arrow_down)
    }


    private fun setAnimation() {
        processManager_iv_drawer1.setImageResource(R.drawable.drawer_arrow_up)
        processManager_iv_drawer2.setImageResource(R.drawable.drawer_arrow_up)
        val alphaAnimation = AlphaAnimation(0.2f, 1.0f)
        alphaAnimation.duration = 500
        alphaAnimation.repeatCount = Animation.INFINITE
        alphaAnimation.repeatMode = Animation.REVERSE
        processManager_iv_drawer1.startAnimation(alphaAnimation)

        //不透明到半透明
        val alphaAnimation1 = AlphaAnimation(1.0f, 0.2f)
        alphaAnimation1.duration = 500
        alphaAnimation1.repeatCount = Animation.INFINITE
        alphaAnimation1.repeatMode = Animation.REVERSE
        processManager_iv_drawer2.startAnimation(alphaAnimation1)

    }

    fun notifyDataSetChanged(runningProcesses: ProcessListBean) {
        val result = SharePreferencesUtils.getBoolean(Constants.SPFILEA, Constants.SHOWSYSTEMPROCESS, false)
        showSystem = result
        if (mAdapter == null) {
            mAdapter = ProcessManagerAdapter(this, runningProcesses)
            processManager_rv_processList.adapter = mAdapter
        }
        mAdapter?.mShowSystem = showSystem
        mAdapter?.notifyDataSetChanged()
    }

    fun showMemoryMsg(memoryInfo: StorageUtil.MemoryInfo) {
        processManager_cp_memory.setText("内存:")
        processManager_cp_memory.setAvaliableMemory(Formatter.formatFileSize(this, memoryInfo.memoryAvail))
        processManager_cp_memory.setUsedMemory(Formatter.formatFileSize(this, memoryInfo.memoryCount - memoryInfo.memoryAvail))
        processManager_cp_memory.setProgress(memoryInfo.usedMemoryRate)
    }

    fun showProcessMsg(bean: ProcessBean) {
        processManager_cp_processes.setText("进程:")
        processManager_cp_processes.setAvaliableMemory((bean.totalSize - bean.runningSize).toString())
        processManager_cp_processes.setUsedMemory(bean.runningSize.toString())
        processManager_cp_processes.setProgress(bean.runningRate)
    }

    fun showAppTitle(s: String) {
        processManager_tv_processTitle.text = s
    }


    fun showToast(size: Int, deleteMemory: Long) {
        toast("清理了$size 进程,释放了${Formatter.formatFileSize(this@ProcessManagerActivity, deleteMemory)}内存")
    }


}