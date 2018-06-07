package com.guard.model.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.text.TextUtils
import com.guard.model.db.AddressDao
import com.guard.ui.customwidgets.AddressToast

class AddressService : Service() {
    var addressToast: AddressToast? = null
    var receiver: CallReceiver? = null
    var manager: TelephonyManager? = null
    var listener: CallPhoneStateListener? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        addressToast = AddressToast(this)
        manager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        listener = CallPhoneStateListener()
        manager?.listen(listener, PhoneStateListener.LISTEN_CALL_STATE)

        receiver = CallReceiver()
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL)
        registerReceiver(receiver, filter)
    }


    inner class CallReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val phone = intent!!.getStringExtra(Intent.EXTRA_PHONE_NUMBER)
            queryAddress(phone)
        }
    }

    inner class CallPhoneStateListener : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, incomingNumber: String?) {
            when (state) {
                TelephonyManager.CALL_STATE_IDLE -> {
                    addressToast?.hideToast()
                }
                TelephonyManager.CALL_STATE_RINGING -> {
                    if (incomingNumber != null)
                        queryAddress(incomingNumber)
                }
                TelephonyManager.CALL_STATE_OFFHOOK -> {

                }
            }
        }
    }


    override fun onDestroy() {
        unregisterReceiver(receiver)
        manager?.listen(listener!!, PhoneStateListener.LISTEN_NONE)
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
