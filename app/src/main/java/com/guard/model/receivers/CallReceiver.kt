package com.guard.model.receivers

import android.content.Context
import java.util.*

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.model.receivers
 * @description: description  https://gist.github.com/ftvs/e61ccb039f511eb288ee
 * @date: 2018/6/4
 * @time: 23:11
 */
class CallReceiver : PhonecallReceiver() {

    override fun onOutgoingCallEnded(context: Context?, saveNumber: String?, callStartTime: Date?, date: Date) {

    }

    override fun onIncomingCallEnded(context: Context?, saveNumber: String?, callStartTime: Date?, date: Date) {
    }

    override fun onMissedCall(context: Context?, saveNumber: String?, callStartTime: Date?) {
    }

    override fun onIncomingCallAnswered(context: Context?, saveNumber: String?, callStartTime: Date) {
    }

    override fun onOutgoingCallStarted(context: Context?, saveNumber: String?, callStartTime: Date) {
    }

    override fun onIncomingCallReceived(context: Context?, number: String?, callStartTime: Date) {
    }
}