package com.guard.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.guard.R
import kotlinx.android.synthetic.main.activity_commontools.*

class CommonToolsActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commontools)
        commontool_sv_address.setOnClickListener(this)
        commontool_sv_commonnumber.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.commontool_sv_address -> startActivity(Intent(this, AddressActivity::class.java))
            R.id.commontool_sv_commonnumber -> startActivity(Intent(this, CommonAddressActivity::class.java))
        }
    }
}
