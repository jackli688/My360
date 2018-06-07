package com.guard.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.guard.R
import com.guard.model.db.BlackNumberDao
import com.guard.model.db.BlackNumberOpenHelper
import kotlinx.android.synthetic.main.activity_black_number_edit.*

class EditBlackNumberActivity : AppCompatActivity() {

    var mBlackNumber: String? = null
    var mInterceptionMOde: Int? = 0

    companion object {
        val RESULTCODE: Int = 11
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBlackNumber = intent.getStringExtra(AddBlackNumberActivity.BLACKNUMBER)
        mInterceptionMOde = intent.getIntExtra(AddBlackNumberActivity.INTERCEPTIONMODE, 0)
        setContentView(R.layout.activity_black_number_edit)
        edit_et_blacknumber.setText(mBlackNumber)
        when (mInterceptionMOde) {
            BlackNumberOpenHelper.BLACKNUMBER_CALL -> {
                edit_rg_modes.check(R.id.edit_rbtn_call)
            }
            BlackNumberOpenHelper.BLACKNUMBER_SMS -> {
                edit_rg_modes.check(R.id.edit_rbtn_sms)
            }
            BlackNumberOpenHelper.BLACKNUMBER_ALL -> {
                edit_rg_modes.check(R.id.edit_rbtn_all)
            }
        }
    }


    fun save(view: View) {
        val tempNumber = edit_et_blacknumber.text.toString().trim()
        val currentMode = getCurrentIntercetionMode(edit_rg_modes.checkedRadioButtonId)
        if (currentMode == -1) {
            Toast.makeText(this@EditBlackNumberActivity, "请选择拦截模式", Toast.LENGTH_SHORT).show()
            return
        }
        if (tempNumber == mBlackNumber && currentMode == mInterceptionMOde) {
            finish()
            return
        } else {
            val updateInfoStatus = @SuppressLint("StaticFieldLeak")
            object : AsyncTask<Void, Void, Boolean>() {
                override fun doInBackground(vararg params: Void?): Boolean {
                    val updata = BlackNumberDao.instance.updata(tempNumber, currentMode)
                    return updata
                }

                override fun onPostExecute(result: Boolean?) {
                    if (result!!) {
                        Toast.makeText(this@EditBlackNumberActivity, "数据更新成功", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@EditBlackNumberActivity, HarassmentInterceptionActivity::class.java)
                        intent.putExtra(AddBlackNumberActivity.BLACKNUMBER, tempNumber)
                        intent.putExtra(AddBlackNumberActivity.INTERCEPTIONMODE, currentMode)
                        setResult(RESULTCODE, intent)
                        finish()
                    }
                }
            }
            updateInfoStatus.execute()
        }
    }

    private fun getCurrentIntercetionMode(checkedRadioButtonId: Int): Int {
        return when (checkedRadioButtonId) {
            R.id.edit_rbtn_call -> {
                BlackNumberOpenHelper.BLACKNUMBER_CALL
            }
            R.id.edit_rbtn_sms -> {
                BlackNumberOpenHelper.BLACKNUMBER_SMS
            }
            R.id.edit_rbtn_all -> {
                BlackNumberOpenHelper.BLACKNUMBER_ALL
            }
            else -> {
                -1
            }
        }
    }

    fun cancel(view: View) {
        finish()
    }

}
