package com.guard.ui.adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.guard.R
import com.guard.model.bean.HomeItemBean

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.ui.adapters
 * @description: description
 * @date: 2018/5/8
 * @time: 23:12
 */
class HomeAdapter() : BaseAdapter() {

    constructor(ctx: Context, resLayout: Int, datas: ArrayList<HomeItemBean>) : this() {
        mContext = ctx
        mLayout = resLayout
        this.datas = datas
    }


    var mContext: Context? = null
    var mLayout: Int? = null
    var datas: ArrayList<HomeItemBean>? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var holder: HomeViewHolder
        var rootView: View
        if (convertView == null) {
            holder = HomeViewHolder()
            rootView = LayoutInflater.from(mContext).inflate(mLayout!!, parent, false)
            holder.icon = rootView.findViewById(R.id.home_item_icon) as ImageView
            holder.title = rootView.findViewById(R.id.home_item_title) as TextView
            holder.desc = rootView.findViewById(R.id.home_item_desc) as TextView
            rootView.tag = holder
        } else {
            rootView = convertView
            holder = rootView.tag as HomeViewHolder
        }
        val homeItemBean = datas?.get(position)
        if (homeItemBean != null) {
            holder.icon.setImageDrawable(ContextCompat.getDrawable(mContext!!, homeItemBean.mIcon!!))
            holder.title.text = homeItemBean.mTitle
            holder.desc.text = homeItemBean.mDesc
        }
        return rootView
    }

    override fun getItem(position: Int): HomeItemBean? {
        return datas?.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return datas?.size ?: 0
    }

    class HomeViewHolder {
        lateinit var icon: ImageView
        lateinit var title: TextView
        lateinit var desc: TextView
    }

}