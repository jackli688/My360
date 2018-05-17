package com.guard.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.guard.R
import com.guard.model.bean.Constants
import com.guard.model.utils.SharePreferencesUtils

class SetUp3Activity : AppCompatActivity() {

    var mInputSafeNumber: EditText? = null
    var safeNumber: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_up3)
        mInputSafeNumber = findViewById(R.id.setup3_et_safenumber)
        safeNumber = SharePreferencesUtils.getString(Constants.SPFILEA, Constants.SAFENUMBER, null)
        mInputSafeNumber?.setText(safeNumber)
    }


    fun next(view: View) {
        safeNumber = mInputSafeNumber!!.editableText.toString()
        if (!mInputSafeNumber!!.editableText.toString().isNullOrEmpty()) {
            SharePreferencesUtils.setString(Constants.SPFILEA, Constants.SAFENUMBER, safeNumber
                    ?: "")
            startActivity(Intent(this, SetUp4Activity::class.java))
            overridePendingTransition(R.anim.setup_next_enter,
                    R.anim.setup_next_exit)
            finish()
        } else {
            Toast.makeText(this, "请输入安全号码", Toast.LENGTH_SHORT).show()
        }
    }

    fun pre(view: View) {
        startActivity(Intent(this, SetUp2Activity::class.java))
        overridePendingTransition(R.anim.setup_pre_enter,
                R.anim.setup_pre_exit)
        finish()
    }

    fun selectContacts(v: View) {
        startActivityForResult(Intent(this, ContactsActivity::class.java), Constants.SETUPASKSAFENUMBEASKRCODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constants.SETUPASKSAFENUMBEASKRCODE -> {
                when (resultCode) {
                    Constants.SETUPASKSAFENUMBERREQUESTCODE -> {
                        val safeNumber = data?.getStringExtra(Constants.SAFENUMBER)
                        mInputSafeNumber?.setText(safeNumber)
                    }
                }
            }
        }
    }

}
