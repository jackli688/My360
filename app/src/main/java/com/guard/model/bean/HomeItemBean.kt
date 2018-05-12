package com.guard.model.bean

import android.util.Log

class HomeItemBean {
    val Tag: String = "HomeItemBean"

    constructor() {
        Log.d(Tag, "main constructor run")
    }

    constructor(icon: Int, title: String, mes: String) : this() {
        Log.d(Tag, "second constructor run")
        mIcon = icon
        mTitle = title
        mDesc = mes
    }

    var mIcon: Int? = null
    var mTitle: String? = null
    var mDesc: String? = null

    init {
//        mIcon = icon
//        mTitleview = title
//        mDesc = mes
        Log.d(Tag, "init run")
    }


}
