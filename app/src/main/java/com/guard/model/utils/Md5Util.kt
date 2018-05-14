package com.guard.model.utils

import java.lang.StringBuilder
import java.security.MessageDigest

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.model.utils
 * @description: description
 * @date: 2018/5/14
 * @time: 12:04
 */
class Md5Util {

    companion object {
        fun msgToMd5(msg: String): String {
            val builder = StringBuilder()
            val messageDigest = MessageDigest.getInstance("MD5")
            val digest = messageDigest.digest(msg.toByteArray())
            for (byte in digest) {
                val result = 0xff and byte.toInt()
                val hexString = Integer.toHexString(result)
                if (hexString.length < 2) {
                    builder.append("0")
                }
                builder.append(hexString)
            }
            return builder.toString()
        }
    }
}