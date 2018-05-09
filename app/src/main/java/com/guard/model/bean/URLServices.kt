package com.guard.model.bean

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard
 * @description: description
 * @date: 2018/5/6
 * @time: 1:25
 */
class URLServices {

    object API_URL {
        val API_URL = "https://api.github.com"
        var Base_Server = "http://192.168.50.121:8080"
    }


    class Contributor(login: String, contributions: Int) {
        val login: String = login
        val contributions: Int = contributions
    }


    interface BasicUrl {
        @GET("/repos/{owner}/{repo}/contributors")
        fun contributors(@Path("owner") owner: String, @Path("repo") repo: String): Call<List<Contributor>>
    }


    class LastVersion {
        var code: Int? = null
        var msg: String? = null
        var versionCode: Int? = null
        var versionName: String? = null
        var apkUrl: String? = null
    }


    interface UpdateService {
        @GET("/mobileguard/updateinfo")
        fun getLastversion(): Call<LastVersion>
    }


    interface DownloadService {

        //case1 下载服务器上固定的资源
        @GET("/player_test/audio/beijingbeijing.mp3")
        fun downloadFile(): Call<ResponseBody>

        //case2   动态的url来下载内容
        @GET
        fun downloadFile(@Url fileUrl: String): Call<ResponseBody>

        //case3  根据动态的url利用流的方式来下载内容
        //利用流读写磁盘的时候，切记不能在主线程中
        @Streaming
        @GET
        fun downloadFileStream(@Url fileUrl: String): Call<ResponseBody>
    }


}