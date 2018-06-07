package com.guard.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Switch
import android.widget.Toast
import com.guard.R
import com.guard.model.db.BlackNumberDao
import com.guard.model.db.BlackNumberOpenHelper
import kotlinx.android.synthetic.main.activity_black_number_add.*

class AddBlackNumberActivity : AppCompatActivity() {
    companion object {
        val RESULTCODE = 10
        val BLACKNUMBER = "blackNumber"
        val INTERCEPTIONMODE = "interceptionMode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_black_number_add)
    }


    fun save(view: View) {
        val currentMode = getCurrentInterceptionMoe()
        when {
            add_et_blacknumber.text.isNullOrEmpty() -> {
                showToast("请输入电话号码")
                return
            }
            currentMode == -1 -> {
                showToast("请选择拦截模式")
                return
            }
            else -> {
                val number = add_et_blacknumber.text.toString().trim()
                val queryTask = @SuppressLint("StaticFieldLeak")
                object : AsyncTask<Void, Void, Int>() {
                    override fun doInBackground(vararg params: Void?): Int {
                        val query = BlackNumberDao.instance.query(number)
                        return query
                    }

                    override fun onPostExecute(result: Int?) {
                        when (result) {
                            -1 -> {
                                object : AsyncTask<Void, Void, Boolean>() {
                                    override fun doInBackground(vararg params: Void?): Boolean {
                                        val add = BlackNumberDao.instance.add(number, currentMode)
                                        return add
                                    }
                                    override fun onPostExecute(result: Boolean?) {
                                        if (result!!) {
                                            val intent = Intent(this@AddBlackNumberActivity, HarassmentInterceptionActivity::class.java)
                                            intent.putExtra(BLACKNUMBER, number)
                                            intent.putExtra(INTERCEPTIONMODE, currentMode)
                                            setResult(RESULTCODE, intent)
                                            finish()
                                        }
                                    }
                                }.execute()
                            }
                            else -> {
                                showToast("此号码已经添加")
                            }
                        }

                    }
                }
                queryTask.execute()
            }
        }
    }


    fun cancel(view: View) {
        finish()
    }


    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


    private fun getCurrentInterceptionMoe(): Int {
        val checkedRadioButtonId = add_rg_modes.checkedRadioButtonId
        when (checkedRadioButtonId) {
            R.id.add_rbtn_call -> {
                return BlackNumberOpenHelper.BLACKNUMBER_CALL
            }
            R.id.add_rbtn_sms -> {
                return BlackNumberOpenHelper.BLACKNUMBER_SMS
            }
            R.id.add_rbtn_all -> {
                return BlackNumberOpenHelper.BLACKNUMBER_ALL
            }
        }
        return -1
    }

}
