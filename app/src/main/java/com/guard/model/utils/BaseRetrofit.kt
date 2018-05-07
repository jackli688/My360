package com.guard.model.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard
 * @description: description
 * @date: 2018/5/6
 * @time: 1:41
 */
class BaseRetrofit {

    companion object {
        fun getRetrofit(url: String): Retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }


}