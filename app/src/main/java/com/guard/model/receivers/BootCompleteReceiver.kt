package com.guard.model.receivers

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import com.guard.model.bean.Constants
import com.guard.model.utils.SharePreferencesUtils

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.model.receivers
 * @description: description
 * @date: 2018/5/16
 * @time: 10:47
 */
class BootCompleteReceiver : BroadcastReceiver() {

    @SuppressLint("HardwareIds")
    override fun onReceive(context: Context?, intent: Intent?) {

        val sim = SharePreferencesUtils.getString(Constants.SPFILEA, Constants.SIMSERIALNUMBER, null)
        if (!TextUtils.isEmpty(sim)) {
            val telephonyManager = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) if (!sim.equals(telephonyManager.simSerialNumber)) {
                //发送报警短信
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage("15201195639", null, "da ge wo bei dao le,help me", null, null)
            }
        }
    }
}