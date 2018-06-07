package com.guard.model.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.*
import android.database.ContentObserver
import android.net.Uri
import android.os.AsyncTask
import android.os.Handler
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.SmsMessage
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import com.android.internal.telephony.ITelephony
import com.guard.model.db.BlackNumberDao
import com.guard.model.db.BlackNumberOpenHelper


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class BlackNumberService : Service() {

    val TAG: String = BlackNumberService::class.java.simpleName
    var smsReceiver: InterceptorSMSReceiver? = null
    var tel: TelephonyManager? = null
    var listener: MyPhoneSateListener? = null


    override fun onCreate() {
        super.onCreate()
        print("onCreate's method is run")
        smsReceiver = InterceptorSMSReceiver()
        val filter = IntentFilter()
        filter.priority = IntentFilter.SYSTEM_HIGH_PRIORITY
        filter.addAction("android.provider.Telephony.SMS_RECEIVED")
        registerReceiver(smsReceiver, filter)

        tel = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        listener = MyPhoneSateListener()
        tel?.listen(listener, PhoneStateListener.LISTEN_CALL_STATE)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        print("\t \t onStartCommand's method is run")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        print("onBind's method is run")
        return null
    }

    override fun unbindService(conn: ServiceConnection?) {
        print("unbindService's method is run")
        super.unbindService(conn)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        print("onUnbind's method is run")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        print("onDestroy's method is run")
        unregisterReceiver(smsReceiver)
        if (tel == null)
            tel = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        tel!!.listen(listener, PhoneStateListener.LISTEN_NONE)
    }

    private fun print(msg: String) {
        Log.e(TAG, msg)
    }

    inner class InterceptorSMSReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            for (any in intent?.extras?.get("pdus") as Array<*>) {
                val smsMessage = SmsMessage.createFromPdu(any as ByteArray)
                val queryTask: AsyncTask<Void, Void, Boolean> = @SuppressLint("StaticFieldLeak")
                object : AsyncTask<Void, Void, Boolean>() {
                    override fun doInBackground(vararg params: Void?): Boolean {
                        val query = BlackNumberDao.instance.query(smsMessage.originatingAddress)
                        if (query == BlackNumberOpenHelper.BLACKNUMBER_SMS || query == BlackNumberOpenHelper.BLACKNUMBER_ALL) {
                            return true
                        }
                        return false
                    }

                    override fun onPostExecute(result: Boolean?) {
                        if (result!!) {
                            abortBroadcast()
                            print("InterceptorSMSReceiver exec abortBroadcast")
                            deletCurrentSms()
                        }
                    }

                }
                queryTask.execute()
            }
        }

    }

    private fun deletCurrentSms() {

    }

    inner class MyPhoneSateListener : PhoneStateListener() {

        override fun onCallStateChanged(state: Int, incomingNumber: String?) {
            when (state) {
                TelephonyManager.CALL_STATE_IDLE -> {

                }
                TelephonyManager.CALL_STATE_RINGING -> {
                    val queryTask: AsyncTask<Void, Void, Boolean> = @SuppressLint("StaticFieldLeak")
                    object : AsyncTask<Void, Void, Boolean>() {
                        override fun doInBackground(vararg params: Void?): Boolean {
                            if (incomingNumber != null) {
                                val mode = BlackNumberDao.instance.query(incomingNumber)
                                if (mode == BlackNumberOpenHelper.BLACKNUMBER_CALL || mode == BlackNumberOpenHelper.BLACKNUMBER_ALL) {
                                    return true
                                }
                                return false
                            }
                            return false
                        }

                        override fun onPostExecute(result: Boolean?) {
                            if (result!!) {
                                endCall()
                                if (!TextUtils.isEmpty(incomingNumber)) {
                                    deleteCurrentCall(incomingNumber!!)
                                }
                            }
                        }
                    }
                    queryTask.execute()
                }

                TelephonyManager.CALL_STATE_OFFHOOK -> {

                }
            }

        }
    }

    private fun deleteCurrentCall(incomingNumber: String) {
        val url = Uri.parse("content://call_log/calls")
        this.contentResolver.registerContentObserver(url, true, object : ContentObserver(Handler()) {
            override fun onChange(selfChange: Boolean, uri: Uri?) {
                var tempUrl: Uri
                when (uri) {
                    null -> tempUrl = url
                    else -> tempUrl = uri
                }
                val tempObserer = this
                val deleteTask: AsyncTask<Void, Void, Boolean> = @SuppressLint("StaticFieldLeak")
                object : AsyncTask<Void, Void, Boolean>() {
                    override fun doInBackground(vararg params: Void?): Boolean {
                        val delete = contentResolver.delete(tempUrl, "number=?", arrayOf(incomingNumber))
                        return delete !== -1
                    }

                    override fun onPostExecute(result: Boolean?) {
                        if (result!!) {
                            contentResolver.unregisterContentObserver(tempObserer)
                        }
                    }

                }
                deleteTask.execute()
            }
        })
    }

    @SuppressLint("PrivateApi")
    private fun endCall() {
        val clazz = Class.forName("android.os.ServiceManager")
        val method = clazz.getMethod("getService", String::class.java)
        val iBinder = method.invoke(null, Context.TELEPHONY_SERVICE) as IBinder
        val iTelephony = ITelephony.Stub.asInterface(iBinder)
        iTelephony.endCall()
    }


}
