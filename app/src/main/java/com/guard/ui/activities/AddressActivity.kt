package com.guard.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.Vibrator
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.guard.R
import com.guard.model.db.AddressDao
import kotlinx.android.synthetic.main.activity_address.*

class AddressActivity : AppCompatActivity() {
    var canVibrate: Boolean = false
    val permission = Manifest.permission.VIBRATE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        val checkSelfPermission = ContextCompat.checkSelfPermission(this, permission)
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 1)
        } else {
            canVibrate = true
        }
        address_et_phone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val phone = s?.toString()
                queryAddress(phone!!)

            }

        })
    }

    @SuppressLint("ServiceCast")
    fun getaddress(view: View) {
        if (TextUtils.isEmpty(address_et_phone.text)) {
            val shakAnimation = AnimationUtils.loadAnimation(this, R.anim.shake)
            address_et_phone.startAnimation(shakAnimation)
            //动态获取震动的权限
            if(canVibrate){
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(100)
            }
            Toast.makeText(this@AddressActivity, "请输入电话号码", Toast.LENGTH_SHORT).show()
            return
        } else {
            val inputNumber = address_et_phone.text.toString().trim()
            queryAddress(inputNumber)
        }
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
                    address_tv_address.text = "归属地:"
                } else {
                    address_tv_address.text = "归属地:$result"
                }
            }

        }
        addressQueryTask.execute(inputNumber)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1 && permissions.isNotEmpty() && permissions[0] == permission) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                canVibrate = true
                Toast.makeText(this, "震动权限已经授权", Toast.LENGTH_SHORT).show()
            } else {
                canVibrate = false
                Toast.makeText(this, "震动权限未授权", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
