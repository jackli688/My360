package com.guard.ui.activities

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.ui.activities
 * @description: description
 * @date: 2018/5/19
 * @time: 0:38
 */

class MyThread : Thread() {

    var mInterface: CallBack? = null
    var mm = arrayOf("", "", "")

    override fun run() {
        super.run()
        var result = 0
        for (i in 1..100) {
            if (i % 2 == 0) {
                result += i
            }
        }
        if (result == 0) {
            mInterface?.failure()
        } else {
            mInterface?.succeeded(result)
        }
    }
}