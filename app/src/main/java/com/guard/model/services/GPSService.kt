package com.guard.model.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.support.v4.content.ContextCompat
import android.telephony.SmsManager
import com.guard.model.bean.Constants
import com.guard.model.bean.URLServices
import com.guard.model.utils.SharePreferencesUtils
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GPSService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val myLocationListener = MyLocationListener()
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.0f, myLocationListener)
        }
    }


    inner class MyLocationListener : LocationListener {

        override fun onLocationChanged(location: Location?) {
            //水平方位
            val accuracy = location?.accuracy
            //获取海拔
            val altitude = location?.altitude
            //定位速度
            val speed = location?.speed
            //定位时间
            val time = location?.time
            val latitude = location?.latitude //获取维度
            val longitude = location?.longitude // 获取经度
            //根据经纬度坐标获取实际地理位置,将位置传递给安全号码
            getLocation(longitude, latitude)
        }

        private fun getLocation(longitude: Double?, latitude: Double?) {
            val retrofit = Retrofit.Builder()
                    .baseUrl(URLServices.API_URL.Location_Server)
                    .addConverterFactory(GsonConverterFactory.create()).build()
            val client = retrofit.create(URLServices.AskLocationService::class.java)
            if (longitude != null && latitude != null) {
                val call = client.askLocation(longitude, latitude)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                        if (response != null && response.isSuccessful) {
                            val body = response.body()
                            val responseBody = body.toString()
                            processJson(responseBody)
                        }
                    }

                })
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

        }

        override fun onProviderEnabled(provider: String?) {

        }

        override fun onProviderDisabled(provider: String?) {

        }
    }

    private fun processJson(responseBody: String) {
        val jsonObject = JSONObject(responseBody)
        val row = jsonObject.getJSONObject("row")
        val result = row.getJSONObject("result")
        val address = result.getString("formatted_address")
        sendSMS(address)
    }


    private fun sendSMS(address: String?) {
        val smsManager = SmsManager.getDefault()
        val safeNumber = SharePreferencesUtils.getString(Constants.SPFILEA, Constants.SAFENUMBER, "")
        smsManager.sendTextMessage(safeNumber, null, "大哥我的位置是:$address", null, null)
        this@GPSService.stopSelf()
    }

}
