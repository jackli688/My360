package com.guard.ui.customwidgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.guard.R
import com.guard.model.bean.Constants
import com.guard.model.utils.SharePreferencesUtils

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.ui.customwidgets
 * @description: description
 * @date: 2018/6/6
 * @time: 12:25
 */
class AddressToast(var context: Context) : Toast(context), View.OnTouchListener {

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val params = WindowManager.LayoutParams()
    private var mRootView: View? = null
    private var startX: Int = 0
    private var startY: Int = 0

    init {
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        params.format = PixelFormat.TRANSLUCENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
        } else {
            params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE
        }
    }


    fun hideToast() {
//      Toast
        if (mRootView != null)
            windowManager.removeView(mRootView)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun showToast(result: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(context)) {
                //6.0系统
                mRootView = LayoutInflater.from(context).inflate(R.layout.address_toast, null, false)
                val mAddress = mRootView?.findViewById<TextView>(R.id.address_toast_tv_address)
                mAddress?.text = result
                val drawable = SharePreferencesUtils.getInt(Constants.SPFILEA, Constants.ADDRESSDIALOGBG, R.drawable.normal)
                mAddress?.setBackgroundResource(drawable)
                mRootView?.setOnTouchListener(this)
                windowManager.addView(mRootView, params)
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event == null) {
            return false
        } else {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
//                    绝对坐标
                    startX = event.rawX.toInt()
                    startY = event.rawY.toInt()
                    //相对坐标
//                    val toInt = event.x.toInt()
//                    val toInt1 = event.y.toInt()
//                    Log.e("addresstoast", "startx:$startX,,,,starty:$startY,,,,,,,,x=$toInt,,,,,y=$toInt1")
                }
                MotionEvent.ACTION_MOVE -> {
                    var newX = event.rawX.toInt()
                    var newY = event.rawY.toInt()
                    params.x += (newX - startX)
                    params.y += (newY - startY)
                    windowManager.updateViewLayout(mRootView, params)
                    startX = newX
                    startY = newY
                }
                MotionEvent.ACTION_UP -> {

                }
            }
        }
        return true
    }
}