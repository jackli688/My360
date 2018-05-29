package com.guard.ui.activities

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.ui.activities
 * @description: description
 * @date: 2018/5/19
 * @time: 0:37
 */
interface CallBack {
    fun succeeded(result: Int)
    fun failure()
}