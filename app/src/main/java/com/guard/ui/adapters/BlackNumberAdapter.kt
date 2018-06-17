package com.guard.ui.adapters

import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.guard.R
import com.guard.model.bean.BlackNumberInfo
import com.guard.model.db.BlackNumberDao
import com.guard.model.db.BlackNumberOpenHelper
import com.guard.ui.activities.HarassmentInterceptionActivity

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.ui.adapters
 * @description: description
 * @date: 2018/6/2
 * @time: 12:16
 */
class BlackNumberAdapter(var context: HarassmentInterceptionActivity, var mData: ArrayList<BlackNumberInfo>?, obs: Observer) : BaseAdapter() {

    var observer: Observer? = null

    init {
        observer = obs
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val bean = mData?.get(position)
        var bViewHolder: BViewHolder?
        var tempConvertView: View?
        if (convertView == null) {
            tempConvertView = LayoutInflater.from(context).inflate(R.layout.item_blacknumber_listview, null, false)
            bViewHolder = BViewHolder()
            bViewHolder.blackNumber = tempConvertView.findViewById(R.id.item_tv_blacknumber) as TextView
            bViewHolder.interceptionMode = tempConvertView.findViewById(R.id.item_tv_mode) as TextView
            bViewHolder.deleteImage = tempConvertView.findViewById(R.id.item_iv_delete) as ImageView
            tempConvertView.tag = bViewHolder
        } else {
            tempConvertView = convertView
            bViewHolder = tempConvertView.tag as BViewHolder?
        }
        bViewHolder?.blackNumber?.text = bean?.blackNumber
        var tempMode: String? = null
        when (bean?.mode) {
            BlackNumberOpenHelper.BLACKNUMBER_CALL -> {
                tempMode = "电话拦截"
            }
            BlackNumberOpenHelper.BLACKNUMBER_SMS -> {
                tempMode = "短信拦截"
            }
            BlackNumberOpenHelper.BLACKNUMBER_ALL -> {
                tempMode = "全部拦截"
            }
        }
        bViewHolder?.interceptionMode?.text = tempMode
        bViewHolder?.deleteImage?.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setMessage("你是否删除黑名单" + bean?.blackNumber)
            builder.setPositiveButton("确定") { dialog, _ ->
                val delete = BlackNumberDao.instance.delete(bean?.blackNumber!!)
                if (delete) {
                    mData?.remove(bean)
                    notifyDataSetChanged()
                    observer?.dateChanged()
                    dialog?.dismiss()
                } else {
                    Toast.makeText(context, "系统繁忙，请稍后再试，亲", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton("取消") { dialog, _ -> dialog?.dismiss() }
            builder.show()
        }
        return tempConvertView!!
    }

    override fun getItem(position: Int): BlackNumberInfo? {
        return mData?.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mData?.size ?: 0
    }

    class BViewHolder {
        var blackNumber: TextView? = null
        var interceptionMode: TextView? = null
        var deleteImage: ImageView? = null

    }


    interface Observer {
        fun dateChanged()
    }
}