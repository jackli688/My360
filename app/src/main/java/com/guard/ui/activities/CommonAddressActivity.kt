package com.guard.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ExpandableListView
import com.guard.R
import com.guard.model.bean.CommonNumberGroupInfo
import com.guard.model.db.CommonNumberDao
import com.guard.ui.adapters.CommonAddressAdapter
import kotlinx.android.synthetic.main.activity_commonaddress.*
import java.lang.ref.WeakReference

class CommonAddressActivity : AppCompatActivity() {

    var mExpandableListView: ExpandableListView? = null
    var mData: List<CommonNumberGroupInfo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commonaddress)
        initView()
        initData()
    }

    var expandPosition: Int = -1
    private fun initView() {
        mExpandableListView = commonnumber_ev_phones
        commonnumber_ev_phones.setOnGroupClickListener(object : ExpandableListView.OnGroupClickListener {
            override fun onGroupClick(parent: ExpandableListView?, v: View?, groupPosition: Int, id: Long): Boolean {
                if   (expandPosition != groupPosition) {
                    commonnumber_ev_phones.collapseGroup(expandPosition)
                }
                expandPosition = if (commonnumber_ev_phones.isGroupExpanded(groupPosition)) {
                    commonnumber_ev_phones.collapseGroup(groupPosition)
                    -1
                } else {
                    commonnumber_ev_phones.expandGroup(groupPosition)
                    groupPosition
                }
                //ture 代表需要拦截这个事件，并且消费掉
                return true
            }
        })
        commonnumber_ev_phones.setOnChildClickListener(object : ExpandableListView.OnChildClickListener {
            override fun onChildClick(parent: ExpandableListView?, v: View?, groupPosition: Int, childPosition: Int, id: Long): Boolean {
                if (mData != null) {
                    val intent = Intent()
                    intent.action = Intent.ACTION_CALL
                    intent.data = Uri.parse("tel:${mData!![groupPosition].list!![childPosition].number}")
                    startActivity(intent)
                }
                return true
            }

        })
    }

    private fun initData() {
        val queryTask = QueryTask(this)
        queryTask.execute()
    }


    class QueryTask(host: CommonAddressActivity) : AsyncTask<Void, Void, List<CommonNumberGroupInfo>>() {

        @SuppressLint("StaticFieldLeak")
        var mHost: CommonAddressActivity? = null

        init {
            mHost = WeakReference<CommonAddressActivity>(host).get()
        }

        override fun doInBackground(vararg params: Void?): List<CommonNumberGroupInfo>? {
            return CommonNumberDao.instances.queryGroup()
        }

        override fun onPostExecute(result: List<CommonNumberGroupInfo>?) {
            if (mHost != null && result != null) {
                mHost!!.mExpandableListView!!.setAdapter(CommonAddressAdapter(result))
                mHost!!.mData = result
            }
        }
    }
}
