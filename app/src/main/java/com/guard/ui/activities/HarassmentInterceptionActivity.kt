package com.guard.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import com.guard.R
import com.guard.model.bean.BlackNumberInfo
import com.guard.model.db.BlackNumberDao
import com.guard.ui.adapters.BlackNumberAdapter
import com.guard.ui.adapters.BlackNumberAdapter.Observer
import kotlinx.android.synthetic.main.activity_black_number.*
import java.lang.ref.WeakReference

class HarassmentInterceptionActivity : AppCompatActivity(), Observer {
    override fun dateChanged() {
        if (mData == null || mData!!.isEmpty()) {
            blacknumber_iv_empty?.visibility = View.VISIBLE
        } else {
            blacknumber_iv_empty?.visibility = View.GONE
        }
    }

    companion object {
        val TAG: String = HarassmentInterceptionActivity::class.java.simpleName

    }

    private var mData: ArrayList<BlackNumberInfo>? = null
    private var mBlackNumberAdapter: BlackNumberAdapter? = null
    private val ADDREQUESTCODE = 8
    private val EDITREQUESTCODE = 9
    private var mInfo: BlackNumberInfo? = null
    private var startIndex: Int = 0
    private val MAXNUM: Int = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_black_number)
//        Thread(Runnable {
//            val blackNumberOpenHelper = BlackNumberOpenHelper(this@HarassmentInterceptionActivity)
//            val writableDatabase = blackNumberOpenHelper.writableDatabase
//            Log.e(TAG, "thread database create execute")
//        }).start()

        blacknumber_iv_add.setOnClickListener {
            startActivityForResult(Intent(this@HarassmentInterceptionActivity, AddBlackNumberActivity::class.java), ADDREQUESTCODE)
        }

        blacknumber_lv_blacknumbers.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this@HarassmentInterceptionActivity, EditBlackNumberActivity::class.java)
            mInfo = mData?.get(position)
            intent.putExtra(AddBlackNumberActivity.BLACKNUMBER, mInfo?.blackNumber)
            intent.putExtra(AddBlackNumberActivity.INTERCEPTIONMODE, mInfo?.mode)
            startActivityForResult(intent, EDITREQUESTCODE)
        }
        blacknumber_lv_blacknumbers.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                Log.e("listview", "onScroll is run ")
            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                Log.e("listview", "current status:$scrollState")
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (blacknumber_lv_blacknumbers.lastVisiblePosition == (mData?.size ?: 0) - 1) {
                        startIndex += MAXNUM
                        initData()
                    }
                }
            }
        })
        initData()
    }

    private fun initData() {
        val blackDbTask = QueryData(this)
        blackDbTask.execute()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            ADDREQUESTCODE -> {
                when (resultCode) {
                    AddBlackNumberActivity.RESULTCODE -> {
                        val number = data?.getStringExtra(AddBlackNumberActivity.BLACKNUMBER)
                        val mode = data?.getIntExtra(AddBlackNumberActivity.INTERCEPTIONMODE, -1)
                        if (mode != -1) {
                            if (mData == null) {
                                mData = ArrayList()
                            }
                            mData?.add(0, BlackNumberInfo(number!!, mode!!))
                            blacknumber_iv_empty.visibility = View.GONE
                            mBlackNumberAdapter?.notifyDataSetChanged()
                        }
                    }
                }
            }

            EDITREQUESTCODE -> {
                when (resultCode) {
                    EditBlackNumberActivity.RESULTCODE -> {
                        val number = data?.getStringExtra(AddBlackNumberActivity.BLACKNUMBER)
                        val mode = data?.getIntExtra(AddBlackNumberActivity.INTERCEPTIONMODE, -1)
                        if (mode != -1) {
                            var notifiy: Boolean = false
                            val iterator = mData?.iterator()
                            while (iterator?.hasNext()!!) {
                                val next = iterator.next()
                                if (next.blackNumber == mInfo?.blackNumber!! && next.mode == mInfo?.mode!!) {
                                    next.blackNumber = number!!
                                    next.mode = mode!!
                                    notifiy = true
                                }
                            }
                            if (notifiy) {
                                mBlackNumberAdapter?.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }

        }

        if (mData == null || mData!!.isEmpty()) {
            blacknumber_iv_empty.visibility = View.VISIBLE
        } else {
            blacknumber_iv_empty.visibility = View.GONE
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    //Kotlin中的泛型
    class QueryData(mActivity: HarassmentInterceptionActivity) : AsyncTask<Void, Void, ArrayList<BlackNumberInfo>>() {
        @SuppressLint("StaticFieldLeak")
        var activity: HarassmentInterceptionActivity? = null

        init {
            activity = WeakReference(mActivity).get()
        }

        override fun onPreExecute() {
            activity?.balcknumber_lin_loading?.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: Void): ArrayList<BlackNumberInfo> {
            val queryAll = BlackNumberDao.instance.queryPat(activity?.MAXNUM!!, activity?.startIndex!!)
            return queryAll
        }

        override fun onPostExecute(result: ArrayList<BlackNumberInfo>?) {
            activity?.balcknumber_lin_loading?.visibility = View.GONE
            if (result == null || result.isEmpty()) {
                Toast.makeText(activity, "没有更多数据了", Toast.LENGTH_SHORT).show()
            } else {
                if (activity?.mData == null) {
                    activity?.mData = result
                } else {
                    activity?.mData!!.addAll(result)
                }
                if (activity?.mBlackNumberAdapter == null) {
                    activity?.mBlackNumberAdapter = BlackNumberAdapter(activity!!, activity?.mData, activity!!)
                    activity?.blacknumber_lv_blacknumbers!!.adapter = activity?.mBlackNumberAdapter
                }
                activity?.mBlackNumberAdapter?.notifyDataSetChanged()
            }
            if (activity?.mData == null || activity?.mData!!.isEmpty()) {
                activity?.blacknumber_iv_empty?.visibility = View.VISIBLE
            } else {
                activity?.blacknumber_iv_empty?.visibility = View.GONE
            }
        }
    }

}
