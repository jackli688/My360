package com.guard.model.utils

import android.content.res.Resources
import android.util.DisplayMetrics
import com.guard.App

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.model.utils
 * @description: description
 * @date: 2018/5/7
 * @time: 22:31
 */
class UIUtils {

    companion object {
        fun getScreenwidth(): Int? = getDisplayMetrics()?.widthPixels

        fun getScreenheight(): Int? = getDisplayMetrics()?.heightPixels

        fun getDensity(): Float? = getDensity(getDisplayMetrics())

        fun getScaledDensity(): Float? = getScaledDensity(getDisplayMetrics())


        fun px2dip(pxValue: Float): Int {
            val density = getDensity()
            return (pxValue / density!! + 0.5f).toInt()
        }

        fun di2px(dipValue: Float): Int {
            val density = getDensity()
            return (dipValue * density!! + 0.5f).toInt()
        }


        fun px2sp(pxValue: Float): Int {
            val scaledDensity = getScaledDensity()
            return (pxValue / scaledDensity!! + 0.5f).toInt()
        }

        fun sp2px(spValue: Float): Int {
            val scaledDensity = getScaledDensity()
            return (spValue * scaledDensity!! + 0.5f).toInt()
        }


        private fun getScaledDensity(dm: DisplayMetrics?): Float? {
            return dm?.scaledDensity
        }

        private fun getDensity(dm: DisplayMetrics?): Float? {
            return dm?.density
        }

        private fun getDisplayMetrics(): DisplayMetrics? {
            return getDisplayMetrics(getResource())
        }

        private fun getDisplayMetrics(res: Resources?): DisplayMetrics? {
            return res?.displayMetrics
        }

        private fun getResource(): Resources? {
            return App.getContext()?.resources
        }
    }

}