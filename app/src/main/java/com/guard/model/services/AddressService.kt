package com.guard.model.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import android.os.IBinder
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import com.guard.model.db.AddressDao
import com.guard.ui.customwidgets.AddressToast

class AddressService : Service() {
    var addressToast: AddressToast? = null
    var moutReceiver: PhoneStateBroadcastReceiver? = null
    val Tag = this::class.java.simpleName

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        Log.e(Tag, "onCreate")
        addressToast = AddressToast(this)
        moutReceiver = PhoneStateBroadcastReceiver()
        val filter = IntentFilter()
        filter.addAction("android.intent.action.PHONE_STATE")
        filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL)
        registerReceiver(moutReceiver, filter)
    }

    /**
     * first step:
     * init a TelephonyManager
     * subclass PhoneStateListener
     * but receive a call from out, ringing  and IDEA state not call back so i use receiver to solve this case
     * https://stackoverflow.com/questions/33340628/android-telephonymanager-the-joys-of-phonestatelistener-and-incoming-numbers/33474220
     */
    inner class PhoneStateBroadcastReceiver : BroadcastReceiver() {
        val Tag = PhoneStateBroadcastReceiver::class.java.simpleName
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
                val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
                Log.e(Tag, "state:$state")
                //外来的电话
                when (state) {
                //响铃阶段
                    TelephonyManager.EXTRA_STATE_RINGING -> {
                        val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                        if (incomingNumber != null)
                            queryAddress(incomingNumber)
                    }
                //挂断状态w
                    TelephonyManager.EXTRA_STATE_OFFHOOK -> {

                    }
                //空闲状态
                    TelephonyManager.EXTRA_STATE_IDLE -> {
                        addressToast?.hideToast()
                    }
                }
                return
            }
            //往外拨打电话
            if (intent.action == Intent.ACTION_NEW_OUTGOING_CALL) {
                val phone = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)
                queryAddress(phone)
            }
        }


    }

    override fun onDestroy() {
        unregisterReceiver(moutReceiver)
    }


    private fun queryAddress(inputNumber: String) {
        val addressQueryTask: AsyncTask<String, Void, String?> = @SuppressLint("StaticFieldLeak")
        object : AsyncTask<String, Void, String?>() {

            override fun doInBackground(vararg params: String?): String? {
                val address = AddressDao.instance.query(params[0]!!)
                return address
            }

            override fun onPostExecute(result: String?) {
                if (TextUtils.isEmpty(result)) {
                    addressToast?.hideToast()
                } else {
                    addressToast?.showToast(result)
                }
            }

        }
        addressQueryTask.execute(inputNumber)
    }
}
