package com.guard.ui.customwidgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.guard.R

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.ui.customwidgets
 * @description: description
 * @date: 2018/5/9
 * @time: 12:32
 */
class SettingItemView(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    var mContext: Context? = null
    var mRootView: View? = null
    var mTitle: TextView? = null
    var mToggle: ImageView? = null

    init {
        mContext = context
        mRootView = LayoutInflater.from(mContext)?.inflate(R.layout.item_setting, null, false)
        mTitle = mRootView?.findViewById(R.id.settingview_tv_title)
        mToggle = mRootView?.findViewById(R.id.settingview_iv_toggle)
        this.addView(mRootView)
    }


    override fun isClickable(): Boolean {
        return true
    }

}