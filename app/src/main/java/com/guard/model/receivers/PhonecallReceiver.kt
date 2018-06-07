package com.guard.model.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import java.util.*


/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.model.receivers
 * @description: description
 * @date: 2018/6/4
 * @time: 22:09
 */
abstract class PhonecallReceiver : BroadcastReceiver() {

    companion object {
        private var lastState = TelephonyManager.CALL_STATE_IDLE
        private var callStartTime: Date? = null
        private var isIncoming: Boolean = false
        private var saveNumber: String? = null
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action!! == "ANDROID.INTENT.ACTION.NEW_OUTGOING_CALL") {
            saveNumber = intent.extras.getString("android.intent.extra.PHONE_NUMBER")
        } else {
            val stateStr = intent.extras.getString(TelephonyManager.EXTRA_STATE)
            val number = intent.extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
            var state = 0
            when (stateStr) {
                TelephonyManager.EXTRA_STATE_IDLE -> state = TelephonyManager.CALL_STATE_IDLE
                TelephonyManager.EXTRA_STATE_OFFHOOK -> state = TelephonyManager.CALL_STATE_OFFHOOK
                TelephonyManager.EXTRA_STATE_RINGING -> state = TelephonyManager.CALL_STATE_RINGING
            }
            onCallStateChanged(context, state, number)
        }
    }

    private fun onCallStateChanged(context: Context?, state: Int, number: String?) {
        if (lastState == state) {
            return
        }
        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                isIncoming = true
                callStartTime = Date()
                saveNumber = number
                onIncomingCallReceived(context, number, callStartTime!!)
            }
            TelephonyManager.CALL_STATE_OFFHOOK -> {
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false
                    callStartTime = Date()
                    onOutgoingCallStarted(context, saveNumber, callStartTime!!)
                } else {
                    isIncoming = true
                    callStartTime = Date()
                    onIncomingCallAnswered(context, saveNumber, callStartTime!!)
                }
            }
            TelephonyManager.CALL_STATE_IDLE -> {
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    onMissedCall(context, saveNumber, callStartTime)
                } else if (isIncoming) {
                    onIncomingCallEnded(context, saveNumber, callStartTime, Date())
                } else {
                    onOutgoingCallEnded(context, saveNumber, callStartTime, Date())
                }
            }
        }
    }

    abstract fun onOutgoingCallEnded(context: Context?, saveNumber: String?, callStartTime: Date?, date: Date)

    abstract fun onIncomingCallEnded(context: Context?, saveNumber: String?, callStartTime: Date?, date: Date)

    abstract fun onMissedCall(context: Context?, saveNumber: String?, callStartTime: Date?)

    abstract fun onIncomingCallAnswered(context: Context?, saveNumber: String?, callStartTime: Date)

    abstract fun onOutgoingCallStarted(context: Context?, saveNumber: String?, callStartTime: Date)

    protected abstract fun onIncomingCallReceived(context: Context?, number: String?, callStartTime: Date)

}