package com.guard.model.receivers

import android.app.admin.DevicePolicyManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.telephony.SmsMessage.createFromPdu
import com.guard.R
import com.guard.model.bean.Constants
import com.guard.model.services.GPSService
import com.guard.model.utils.SharePreferencesUtils

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.model.receivers
 * @description: description
 * @date: 2018/5/16
 * @time: 19:43
 */
class SMSReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val arrayOfAnys = intent?.extras?.get("pdus") as Array<*>
        for (arrayOfAny in arrayOfAnys) {
            val smsMessage = createFromPdu(arrayOfAny as ByteArray)
            //获取发件人
            val sender = smsMessage.originatingAddress
            val safeNumber = SharePreferencesUtils.getString(Constants.SPFILEA, Constants.SAFENUMBER, null)
            if (sender == safeNumber) {
                //获取短信的内容
                val body = smsMessage.messageBody
                sendMessage(context, body)
            }
            abortBroadcast()
        }
    }

    private fun sendMessage(context: Context?, body: String?) {
        val policyManager = context?.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val componentName = ComponentName(context, AdminReceiver::class.java)
        when (body) {
            "#*location*#" -> {
                //执行的GPS追踪 实现定位操作，service
                context?.startService(Intent(context, GPSService::class.java))
                abortBroadcast()
            }
            "#*wipedata*#" -> {
                //远程销毁数据
                policyManager.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE)
                abortBroadcast()
            }
            "#*alarm*#" -> {
                //播放报警音乐
                val mediaPlayer = MediaPlayer.create(context, R.raw.ylzs)
                if (mediaPlayer.isPlaying) {
                    return
                } else {
                    mediaPlayer.setVolume(1.0f, 1.0f)
                    mediaPlayer.isLooping = true
                    mediaPlayer.start()
                }
            }
            "#*lockscreen*#" -> {
                //远程锁屏
                if (policyManager.isAdminActive(componentName)) {
                    policyManager.lockNow() //锁屏操作
                    abortBroadcast()
                }
            }
        }
    }
}