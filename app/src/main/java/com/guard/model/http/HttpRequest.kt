package com.guard.model.http

import retrofit2.http.POST

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.model.http
 * @description: description
 * @date: 2018/6/14
 * @time: 0:56
 */
abstract class HttpRequest {
    enum class Type { Get, Post, Put, Delete }

    var mType: Type = Type.Get
    open fun Request() {
        when (mType) {
            Type.Get -> {
                get()
            }
            Type.Post -> {
                post()
            }
            Type.Delete -> {

            }
            Type.Put -> {

            }
        }
    }

    open fun Request(params: LinkedHashMap<String, String>) {
        when (mType) {
            Type.Get -> {
                get(params)
            }
            Type.Post -> {
                post(params)
            }
            Type.Delete -> {

            }
            Type.Put -> {

            }
        }
    }

    abstract fun get()
    abstract fun get(params: LinkedHashMap<String, String>)
    abstract fun post()
    abstract fun post(params: LinkedHashMap<String, String>)
}