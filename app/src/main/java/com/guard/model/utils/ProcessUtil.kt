package com.guard.model.utils

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import com.guard.App
import com.guard.R
import com.jaredrummler.android.processes.AndroidProcesses
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.model.utils
 * @description: Android不同版本获取正在运行的进程的方式不太一样，所以这里，暂时先放下
 * @date: 2018/6/21
 * @time: 17:09
 */
class ProcessUtil {
    companion object {
        private var isLike5X = false
        /**
         * Android不同版本的兼容性
         * https://juejin.im/post/5947bb4eac502e5490e46a52
         */
        fun getRunningProcess(context: Context): List<ProcessBean> {
            val tempContext = App.getContext(context)
            var processList: ArrayList<ProcessBean> = ArrayList()
            return when {
                Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP -> getKitProcess(tempContext, processList)
                Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M -> getLollipopProcess(processList)
                Build.VERSION_CODES.M <= Build.VERSION.SDK_INT -> getTestProcess(processList)
//                    getMProcess(processList)
                else -> processList
            }
        }

        private fun getTestProcess(processList: ArrayList<ProcessBean>): List<ProcessBean> {
            if (!processList.isEmpty())
                processList.clear()
            for (i in IntRange(0, 100)) {
                val processBean = ProcessBean()
                processBean.name = "currentName$i"
                processBean.name = "com.google.test$i"
                processBean.pid = 1024 + i
                processBean.memorySize = Math.round(Math.random() * 100) * 1024 * 1024
                processBean.icon = App.getContext().resources.getDrawable(R.drawable.ic_default)
                processBean.isSystem = i % 2 == 1
                processList.add(processBean)
            }
            return processList
        }


        private fun getMProcess(processList: ArrayList<ProcessBean>): List<ProcessBean> {
            //正式取可用的Android进程
            var reader: BufferedReader? = null
            try {
                val execBuilder = ProcessBuilder("sh", "-C", "ps")
                execBuilder.redirectErrorStream(true)
                val exec = execBuilder.start()
                val inputStream = exec.inputStream
                reader = BufferedReader(InputStreamReader(inputStream))
                var line: String?
                while (true) {
                    line = reader.readLine()
                    if (line != null) {
                        val array = line.trim().split("\\s+")
                        if (array.size >= 9) {
                            if (array[8] == "su" || array[8] == "sh" || array[8] == "sush" || array[8] == "ps") {
                                continue
                            }
                            var uid = -1
                            try {
                                uid = array[0].substring(4).toInt()
                            } catch (e: Exception) {
                                //如果异常了，说明取到了系统进程信息，则判定为6.x系统的策略和5.x一致，应该采用5.x的策略
                                isLike5X = true
                                reader.close()
                                return getLollipopProcess(processList)
                            }
                            val pid = array[1].toInt()
                            val ppid = array[2].toInt()
                            Log.d("ProcessUtil", "pid:$pid---------array[8]:$array[8]")
                        }
                    } else {
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                reader?.close()
            }
            return processList
        }

        private fun getKitProcess(tempContext: Context, processList: ArrayList<ProcessBean>): ArrayList<ProcessBean> {
            val am = tempContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val pm = tempContext.packageManager
            for (runningAppProcess in am.runningAppProcesses) {
                val processBean = ProcessBean()
                val packageName = runningAppProcess.processName
                processBean.packageName = packageName
                processBean.pid = runningAppProcess.pid
                val processMemoryInfo = am.getProcessMemoryInfo(arrayOf(runningAppProcess.pid).toIntArray())
                val totalPss = processMemoryInfo[0].totalPss
                val memorySize: Long = totalPss * 1024L
                processBean.memorySize = memorySize

                try {
                    val applicationInfo = pm.getApplicationInfo(packageName, 0)
                    val appName = applicationInfo.loadLabel(pm).toString()
                    processBean.name = appName
                    processBean.icon = applicationInfo.loadIcon(pm)
                    val isSystem = applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == ApplicationInfo.FLAG_SYSTEM
                    processBean.isSystem = isSystem
                } catch (e: PackageManager.NameNotFoundException) {
                    processBean.name = packageName
                    processBean.icon = tempContext.resources.getDrawable(R.drawable.ic_default)
                    processBean.isSystem = true
                } finally {
                    processList.add(processBean)
                }
            }
            return processList
        }

        private fun getLollipopProcess(processList: ArrayList<ProcessBean>): ArrayList<ProcessBean> {
            //先获取Android应用进程的父进程zygote进程号，64位app对应的是zygote64
            var zygotePid = -1
            var zygotePid64 = -1
            var readerZ: BufferedReader? = null

            try {
                val execBuilderZ = ProcessBuilder("sh", "-c", "ps |grep zygote")
                execBuilderZ.redirectErrorStream(true)
                val execZ = execBuilderZ.start()
                val isZ = execZ.inputStream
                readerZ = BufferedReader(InputStreamReader(isZ))
                var lineZ: String?
                while (true) {
                    lineZ = readerZ.readLine()
                    if (lineZ != null) {
                        val arrayZ = lineZ.trim().split("\\s+")
                        if (arrayZ.size >= 9) {
                            if (arrayZ[8] === "zygote") {
                                zygotePid = arrayZ[1].toInt()
                            } else if (arrayZ[8] === "zygote64") {
                                zygotePid64 = arrayZ[1].toInt()
                            }
                        }
                    } else {
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                readerZ?.close()
                processList
            }
            if (zygotePid < 0) {
                processList
            }

            var reader: BufferedReader? = null
            try {
                var execBuilder: ProcessBuilder? = null
                execBuilder = ProcessBuilder("sh", "-c", "ps |grep u0_a")
                execBuilder.redirectErrorStream(true)
                var exec: Process?
                exec = execBuilder.start()
                val inputStream = exec?.inputStream
                reader = BufferedReader(InputStreamReader(inputStream))
                var line: String?
                while (true) {
                    line = reader.readLine()
                    if (line != null) {
                        val array = line.trim().split("\\s+")
                        if (array.size >= 9) {
                            //大于10000的uid会以u0_a开头，10021显示u0_a21
                            //非系统应用的程序已u0_a开头,用户的uid都大于10000
                            val uid = (array[0].substring(4) + 1000).toInt()
                            val pid = array[1].toInt()
                            val ppid = array[2].toInt()
                            //过滤掉系统子进程，只留下了父进程是zygote的进程
                            if (ppid == zygotePid || ppid == zygotePid64) {
                                val processBean = ProcessBean()
                                processBean.pid = pid
                                Log.d("ProcessUtil", "pid:$pid-------------------array[8]:$array[8]")
                            }
                        }
                    } else {
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                reader?.close()
                return processList
            }
        }


        fun getALLProcess(context: Context): List<PackageInfo> {
            val pm = App.getContext(context).packageManager
            return pm.getInstalledPackages(0)
        }


        fun killProcess(pkgName: String?): Boolean {
            val tempContext = App.getContext()
            val am = tempContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            return try {
                if (pkgName != null) {
                    val method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String::class.java)
                    method.invoke(am, pkgName)
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }


        fun killProcesses() {
            //清除除自身以外，所有正在运行的进程
            Log.d(this::class.java.simpleName, "killProcesses is run")
        }

        //第三方库来实现获取正在运行的进程，但是经过测试目前支支持到Android5.X
        fun getAllRunningProcess(processList: ArrayList<ProcessBean>): List<ProcessBean> {
            //Get a list of running apps
            val processes = AndroidProcesses.getRunningAppProcesses()
            for (process in processes) {
                //Get some information about the process
                Log.d("ProcessUtil", "processName:${process.name}")
            }
            return processList
        }


    }

    open class ProcessBean {
        var name: String? = null
        var packageName: String? = null
        var pid: Int? = null
        var memorySize: Long = 0.toLong()
        var icon: Drawable? = null
        var isSystem: Boolean = false
        var isChecked: Boolean = false
    }

}
