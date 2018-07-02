package com.guard.persenter

import android.view.View

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.persenter
 * @description: description
 * @date: 2018/6/22
 * @time: 22:58
 */
interface IBasePresenter<T> {

    fun onAttach(t: T)

    fun onDetach()

    fun loadData()


    interface SoftManagerPresenter<T> : IBasePresenter<T> {
        fun judgeTitleWhatShow(position: Int)
        fun judgeShowPopupWindow(v: View, position: Int)
    }


    interface ProcessManagerPresenter<T> : IBasePresenter<T> {
        fun judgeTitleWhatShow(position: Int)

    }
}