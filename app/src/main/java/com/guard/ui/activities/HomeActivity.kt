package com.guard.ui.activities

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.GridView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.guard.App
import com.guard.R
import com.guard.model.bean.HomeItemBean
import com.guard.ui.adapters.HomeAdapter

/**
 * https://www.kotlincn.net/docs/reference/keyword-reference.html?q=&p=0#%E7%A1%AC%E5%85%B3%E9%94%AE%E5%AD%97
 */
class HomeActivity : AppCompatActivity() {

    var mHomeLogo: ImageView? = null
    var mHomeSetting: ImageButton? = null
    private var mHomeGridView: GridView? = null
    private val Titles: Array<String> = arrayOf("手机防盗", "骚扰拦截", "软件管家", "进程管理", "流浪统计", "手机杀毒", "缓存清理", "常用工具")
    private val Descs: Array<String> = arrayOf("远程定位手机", "全面拦截骚扰", "管理您的软件", "管理运行进程", "流量一目了然", "病毒无处藏身", "系统快如火箭", "工具大全")
    private val Icons: IntArray = intArrayOf(R.drawable.sjfd, R.drawable.srlj, R.drawable.rjgj, R.drawable.jcgl, R.drawable.lltj, R.drawable.sjsd, R.drawable.hcql, R.drawable.cygj)
    private val ItemDatas = ArrayList<HomeItemBean>(8)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initView()
        initData()
        initProperty()
    }

    private fun initProperty() {
        setLogoAnimation(mHomeLogo)
        initAdapter()
        setOnListener()
    }

    private fun initAdapter() {
        mHomeGridView?.adapter = HomeAdapter(App.getContext(this), R.layout.item_home_gridview, ItemDatas)
    }

    private fun setOnListener() {
        mHomeSetting?.setOnClickListener({
            enterSettingActivity()
        })
        mHomeGridView?.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, "当前条目点击的位置是:" + position, Toast.LENGTH_SHORT).show()
            when (position) {
                0 -> enterAntiTheftActivity()
                1 -> enterHarassmentInterceptionActivity()
                2 -> enterSoftManagerActivity()
                3 -> enterProcessManagerActivity()
                4 -> enterTrafficStatisticsActivity()
                5 -> enterClearAntivirusActivity()
                6 -> enterClearCacheActivity()
                7 -> enterCommonToolsActivity()

            }
        }
    }

    private fun enterClearCacheActivity() {

    }

    private fun enterClearAntivirusActivity() {

    }

    private fun enterTrafficStatisticsActivity() {

    }

    private fun enterProcessManagerActivity() {

    }

    private fun enterSoftManagerActivity() {

    }

    private fun enterHarassmentInterceptionActivity() {

    }

    private fun enterAntiTheftActivity() {

    }

    private fun enterCommonToolsActivity() {

    }

    private fun enterSettingActivity() {
        startActivity(Intent(this, SettingActivity::class.java))
    }

    private fun setLogoAnimation(aimView: ImageView?) {
        val logoAnimation = ObjectAnimator.ofFloat(aimView, "rotationY", 0f, 270f, 360f)
        logoAnimation.repeatCount = ObjectAnimator.INFINITE
        logoAnimation.duration = 2000
        logoAnimation.repeatMode = ObjectAnimator.RESTART
        logoAnimation.start()
    }

    private fun initView() {
        mHomeLogo = findViewById(R.id.home_lv_logo)
        mHomeSetting = findViewById(R.id.home_setting)
        mHomeGridView = findViewById(R.id.home_gridView)
    }


    private fun initData() {
        for (i in 0 until 8) {
            ItemDatas.add(HomeItemBean(Icons[i], Titles[i], Descs[i]))
        }

    }


}
