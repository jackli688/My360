package com.guard.ui.activities

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.guard.R
import com.guard.model.bean.Constants
import com.guard.model.bean.URLServices
import com.guard.model.utils.BaseRetrofit
import com.guard.model.utils.PackageUtil
import com.guard.model.utils.SharePreferencesUtils
import com.guard.ui.customwidgets.DownLoadDialog
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.*
import java.lang.ref.WeakReference

/**
 * https://futurestud.io/tutorials/retrofit-2-how-to-download-files-from-server
 */
class SplashActivity : AppCompatActivity() {

    var localCode: Int = 0
    var versionBean: URLServices.LastVersion? = null
    var downLoadDialog: DownLoadDialog? = null
    private var mHandler: SplashHandler = SplashHandler(this)
    private var savePath: String? = null
    private val REQUESTCODE = 1
    private val Tag = SplashActivity::class.java.simpleName!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        requestPermission()
        val mRootView: ConstraintLayout = findViewById(R.id.splash_rootView)
        //View :展示动画
        executeAnimator(mRootView)
    }

    private fun requestPermission() {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@SplashActivity, permission)) {

            } else {
                //进行授权
                ActivityCompat.requestPermissions(this, Array(1, { permission }), 1)
            }
        } else {
            copyDb("address.db")
        }

    }


    @SuppressLint("SdCardPath")
    private fun copyDb(name: String) {
        val copyTask: AsyncTask<Void, Void, Void> = @SuppressLint("StaticFieldLeak")
        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
//                val outFileName = "/data/data/$packageName/databases"
                val outFileName = filesDir
                val file = File(outFileName, name)
                var myInutStream: InputStream? = null
                var bops: BufferedOutputStream? = null
                if (!file.exists()) {
                    file.createNewFile()
                    try {
                        myInutStream = assets.open(name)
//                        val filesDir = this.filesDir
//                        Log.e("111", "filesDir:$filesDir")
                        bops = BufferedOutputStream(FileOutputStream(file))
                        val bytes = ByteArray(4096)
                        var len = -1
                        while (true) {
                            len = myInutStream.read(bytes, 0, bytes.size)
                            if (len == -1) {
                                break
                            } else {
                                bops.write(bytes, 0, len)
                            }
                        }
                        bops.flush()
                        Log.e(Tag, name + "copy succeed")
                    } catch (e: IOException) {
                        e.printStackTrace()
                        file.delete()
                    } finally {
                        myInutStream?.close()
                        bops?.close()
                    }

                }
                return null
            }
        }
        copyTask.execute()
    }


    private fun enterHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun showUpdateDialog(apkUrl: String?) {
        AlertDialog.Builder(this).setTitle("发现一个新版本")
                .setMessage("新版本，更快，更强")
                .setPositiveButton("确认") { dialog, which ->
                    if (TextUtils.isEmpty(apkUrl)) {
                        enterHomeActivity()
                        dialog?.dismiss()
                    } else {
                        dialog?.dismiss()
                        //开始下载
                        downloadApk(apkUrl)
                        //现在新的应用，显示下载进度
                        showDownloadDialog()
                    }
                }
                .setNegativeButton("取消") { dialog, which ->
                    enterHomeActivity()
                    dialog?.dismiss()
                }
                .show()
    }

    private fun downloadApk(apkUrl: String?) {
//        "http://192.168.50.121:8080/player_test/audio/beijingbeijing.mp3"
        val builder = Retrofit.Builder().baseUrl(URLServices.API_URL.Base_Server)
        val retrofit = builder.build()
        val downloadService = retrofit.create(URLServices.DownloadService::class.java)
        val call = downloadService.downloadFileStream(apkUrl!!)
//        val call = downloadService.downloadFile()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                Log.e("lijiwei", "currentThread:" + Thread.currentThread().name + ",request result:( no")
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                Log.e("lijiwei", "currentThread:" + Thread.currentThread().name + ",request result:( yes")
//                object : AsyncTask<Void, Void, Void>() {
//                    override fun doInBackground(vararg params: Void?): Void? {
//                        val writeResponseBodyToDisk = writeResponseBodyToDisk(response?.body())
//                        Log.e("lijiwei", "下载的文件结果:" + writeResponseBodyToDisk.toString())
//                        return null
//                    }
//                }.execute()
                object : Thread() {
                    override fun run() {
                        super.run()
                        val split = apkUrl.split("/")
                        val size = split.size
                        val name = split[size - 1]
                        val writeResponseBodyToDisk = writeResponseBodyToDisk(name, response?.body())
                        if (writeResponseBodyToDisk) {
                            installApk()
                        }
                        Log.e("lijiwei", "下载的文件结果:" + writeResponseBodyToDisk.toString())
                    }
                }.start()
//                val writeResponseBodyToDisk = writeResponseBodyToDisk(response?.body())
//                Log.e("lijiwei", "下载的文件结果:" + writeResponseBodyToDisk.toString())
            }
        })
    }

    private fun installApk() {
        val file = File(savePath)
        val intent = Intent()
        intent.action = android.content.Intent.ACTION_VIEW
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            intent.setDataAndType(FileProvider.getUriForFile(baseContext, baseContext.packageName +
                    ".com.guard.model.providers.genericfileprovider", file),
                    "application/vnd.android.package-archive")
        } else {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        }
        startActivityForResult(intent, REQUESTCODE)
    }

    private fun writeResponseBodyToDisk(name: String, body: ResponseBody?): Boolean {
        savePath = Environment.getExternalStorageDirectory().toString() + File.separator + name
        val file = File(savePath)
        val fileReader = ByteArray(4096)
        val length = body?.contentLength()
        var fileDownloaded = 0
        val inputSteam = body?.byteStream()
        val outputStream = BufferedOutputStream(FileOutputStream(file))
        while (true) {
            val read = inputSteam?.read(fileReader)
            if (read == -1) {
                break
            }
            read?.let { outputStream.write(fileReader, 0, it) }
            fileDownloaded += read!!
            val download = fileDownloaded * 100.0f / length!!
            Log.e("lijiwei", "现在进度:" + download.toInt())
            val message = Message()
            message.what = message.arg1
            message.obj = download.toInt()
            if (download.toInt() == 100) {
                mHandler.sendMessageDelayed(message, 200)
            } else {
                mHandler.sendMessage(message)
            }
        }
        outputStream.flush()
        outputStream.close()
        return true
    }

    private fun showDownloadDialog() {
        downLoadDialog = DownLoadDialog(this)
        downLoadDialog?.setCancelable(false)
        downLoadDialog?.show()
    }

    private fun checkLocalversionCode(): Int {
        return PackageUtil.version.getVersionCode(this)
    }

    private fun executeAnimator(mRootView: View) {

        val alphaAnimator = ObjectAnimator.ofFloat(
                mRootView,
                "alpha",
                0.0f,
                1.0f
        )
        alphaAnimator.duration = 3000
        alphaAnimator.startDelay = 500

        alphaAnimator.run {
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    //model:检查是否有最新版本，
                    remoteApkmsg()
                    //获取应用当前的版本号
                    localCode = checkLocalversionCode()
                }

                override fun onAnimationEnd(animation: Animator?) =
                        if (versionBean?.versionCode != null
                                && versionBean?.versionCode!! > localCode
                                && SharePreferencesUtils.getBoolean(Constants.SPFILEA, Constants.SETTING_AUTOUPDATE, false)) {
                            showUpdateDialog(versionBean!!.apkUrl)
                        } else {
                            enterHomeActivity()
                        }
            })
            start()
        }
    }

    private fun remoteApkmsg() {
        val retrofit: Retrofit = BaseRetrofit.getRetrofit(URLServices.API_URL.Base_Server)
        val updateService = retrofit.create(URLServices.UpdateService::class.java)
        val call = updateService.getLastversion()
        call.enqueue(object : Callback<URLServices.LastVersion> {
            override fun onFailure(call: Call<URLServices.LastVersion>?, t: Throwable?) {
                Log.d("lijiwei", "访问出错,t:" + t?.stackTrace)
                versionBean = null
            }

            override fun onResponse(call: Call<URLServices.LastVersion>?, response: Response<URLServices.LastVersion>?) {
                if (response?.isSuccessful!!) {
                    versionBean = response.body()!!
                }
            }

        })
    }


    class SplashHandler(activity: SplashActivity) : Handler() {
        private val mActivity: WeakReference<SplashActivity> = WeakReference(activity)

        override fun handleMessage(msg: Message?) {
            if (mActivity.get() == null) {
                return
            }
            val activity = mActivity.get()
            when (msg?.what) {
                msg?.arg1 -> {
                    val downLoadpercent = msg?.obj as Int
                    if (downLoadpercent == 100) {
                        activity?.downLoadDialog?.dismiss()
                    } else {
                        activity?.downLoadDialog?.setDownloadprogress(downLoadpercent)

                    }
                }
            }
        }
    }


    override fun onDestroy() {
        SplashHandler(this).removeCallbacksAndMessages(null)
        super.onDestroy()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUESTCODE) {
            enterHomeActivity()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}
