package com.guard.model.utils

import android.app.ActivityManager
import android.content.Context
import android.nfc.Tag
import android.os.Build
import android.os.Environment
import android.os.Environment.*
import android.os.StatFs
import android.text.format.Formatter
import android.util.Log
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/**
 * @author: jacklis
 * @version: V1.0
 * @project: My360
 * @package: com.guard.model.utils
 * @description: description
 * @date: 2018/6/19
 * @time: 12:30
 */
class StorageUtil {

    companion object {


        private fun getTotalMemory1(): String {
            val dir = "/proc/meminfo"
            try {
                val fr = FileReader(dir)
                val br = BufferedReader(fr, 2048)
                val memoryLine = br.readLine()
                val subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"))
                br.close()
                val totalMemorySize = Integer.parseInt(subMemoryLine.replace("\\D+".toRegex(), "")).toLong()
//            val availableSize = getAvailableMemory(this@MainActivity) / 1024
//            val percent = ((totalMemorySize - availableSize) / totalMemorySize.toFloat() * 100).toInt()
                return totalMemorySize.toString()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return "无结果"
        }

        //读取系统的文件，同时重新温习了字符中仓用操作方法,以及正则表达式
        private fun getTotalMemory2(context: Context): String {
            val str1 = "/proc/meminfo"// 系统内存信息文件
            val str2: String
            val arrayOfString: Array<String>
            var initial_memory: Long = 0

            try {
                val localFileReader = FileReader(str1)
                val localBufferedReader = BufferedReader(
                        localFileReader, 8192)
                str2 = localBufferedReader.readLine()// 读取meminfo第一行，系统总内存大小

                arrayOfString = str2.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (num in arrayOfString) {
                    Log.i(str2, "$num/t")
                }

                initial_memory = (Integer.valueOf(arrayOfString[1]).toInt() * 1024).toLong()// 获得系统总内存，单位是KB，乘以1024转换为Byte
                localBufferedReader.close()

            } catch (e: IOException) {
            }

            return Formatter.formatFileSize(context, initial_memory)// Byte转换为KB或者MB，内存大小规格化
        }

        fun getMemoryInfo(context: Context): MemoryInfo {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)

            return MemoryInfo(memoryInfo.totalMem, memoryInfo.availMem)
        }


        fun getInternalStorgeInfo(context: Context): InternalStorgeInfo {
            val dataDirectory = getDataDirectory()
            val statFs = StatFs(dataDirectory.path)
            val blockCount: Long
            val blockAvail: Long
            val blockSize: Long
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockCount = statFs.blockCountLong
                blockAvail = statFs.availableBlocksLong
                blockSize = statFs.blockSizeLong
            } else {
                blockCount = statFs.blockCount.toLong()
                blockAvail = statFs.availableBlocks.toLong()
                blockSize = statFs.blockSize.toLong()
            }
            val totalSize = blockCount * blockSize
            val availSize = blockAvail * blockSize
            return InternalStorgeInfo(totalSize, availSize)
        }


        fun getExternalStorgeInfo(): ExternalStorgeInfo {
            if (externalMemoryAvailable()) {
                val externalStorageDirectory = Environment.getExternalStorageDirectory()
                val statFs = StatFs(externalStorageDirectory.path)
                val blockCount: Long
                val blockAvail: Long
                val blockSize: Long
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    blockCount = statFs.blockCountLong
                    blockAvail = statFs.availableBlocksLong
                    blockSize = statFs.blockSizeLong
                } else {
                    blockCount = statFs.blockCount.toLong()
                    blockAvail = statFs.availableBlocks.toLong()
                    blockSize = statFs.blockSize.toLong()
                }
                val totalSize = blockCount * blockSize
                val availSize = blockAvail * blockSize
                return ExternalStorgeInfo(totalSize, availSize)
            } else {
                throw(Exception("not found sdCard,please checkout again!!!"))
            }
        }


        fun externalMemoryAvailable(): Boolean {
            return getExternalStorageState() == MEDIA_MOUNTED
        }

    }

    class InternalStorgeInfo(totalSize: Long, availSize: Long) {
        var totalSize: Long = 0
        var availSize: Long = 0

        init {
            this.totalSize = totalSize
            this.availSize = availSize
        }

        fun getAvailRate(): Int {
            return if (this.availSize == 0L) {
                0
            } else {
                Math.round((availSize.toFloat() / totalSize))
            }
        }

        fun getUsedRate(): Int {
            return 100 - getAvailRate()
        }
    }


    class MemoryInfo(count: Long, avail: Long) {
        var memoryCount: Long = 0L
        var memoryAvail: Long = 0L

        init {
            memoryCount = count
            memoryAvail = avail
        }

        fun getAvailRate(): Int {
            return if (0L == memoryAvail) {
                0
            } else {
                Math.round((memoryAvail.toFloat() * 100 / memoryCount))
            }
        }

        fun getUsedRate(): Int {
            val availRate = getAvailRate()
            Log.i("getUsedRate", "-------:$availRate")
            return 100 - getAvailRate()
        }
    }

    class ExternalStorgeInfo(totalSize: Long, availSize: Long) {
        var totalSize: Long = 0
        var availSize: Long = 0

        init {
            this.totalSize = totalSize
            this.availSize = availSize
        }

        fun getAvailRate(): Int {
            return if (this.availSize == 0L) {
                0
            } else {
                Math.round((availSize.toFloat() * 100 / totalSize))
            }
        }

        fun getUsedRate(): Int {
            return 100 - getAvailRate()
        }
    }


    class SDCardNotMounted(msg: String) : RuntimeException()
}

