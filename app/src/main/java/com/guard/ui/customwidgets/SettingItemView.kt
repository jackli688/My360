package com.guard.ui.customwidgets

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
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
class SettingItemView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : RelativeLayout(context, attrs, defStyleAttr) {

    private var mRootView: View? = null
    private var mTitleview: TextView? = null
    private var mToggleview: ImageView? = null
    private var isToggle: Boolean = false
    private var showToggle: Boolean = true

    constructor(context: Context?) : this(context, null, 0)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)


    init {
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.SettingItemView)
        val titleName = typedArray?.getString(R.styleable.SettingItemView_titleText)
        isToggle = typedArray?.getBoolean(R.styleable.SettingItemView_isToggle, false) ?: false
        showToggle = typedArray?.getBoolean(R.styleable.SettingItemView_showToggle, true) ?: true
        typedArray?.recycle()
        mRootView = LayoutInflater.from(context)?.inflate(R.layout.item_setting, null, false)
        mTitleview = mRootView?.findViewById(R.id.settingview_tv_title) as TextView
        mToggleview = mRootView?.findViewById(R.id.settingview_iv_toggle) as ImageView
        this.addView(mRootView)
        setTitle(titleName)
        setToggle(isToggle)
        setShowToggle(showToggle)
    }

    /**
     * question:加上这两句，设置点击事件，点击事件不起作用
     */
//    override fun isClickable(): Boolean {
//        return true
//    }
//
//    override fun isFocused(): Boolean {
//        return true
//    }

    fun getTitle(): String? {
        return mTitleview?.text.toString()
    }

    fun setTitle(title: String?) {
        mTitleview?.text = title
    }

    fun getToggle(): Boolean {
        return isToggle
    }

    fun setToggle(result: Boolean) {
        isToggle = result
        if (isToggle)
            mToggleview?.setImageResource(R.drawable.on)
        else
            mToggleview?.setImageResource(R.drawable.off)
    }

    fun getShowToggle(): Boolean? {
        return showToggle
    }

    fun setShowToggle(isShow: Boolean) {
        if (isShow)
            mToggleview?.visibility = View.VISIBLE
        else
            mToggleview?.visibility = View.GONE
    }


}