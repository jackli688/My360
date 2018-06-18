package com.guard.ui.adapters

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.guard.App
import com.guard.model.bean.CommonNumberGroupInfo

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.ui.adapters
 * @description: description
 * @date: 2018/6/17
 * @time: 17:53
 */
class CommonAddressAdapter(var mData: List<CommonNumberGroupInfo>) : BaseExpandableListAdapter() {

    override fun getGroup(groupPosition: Int): Any {
        return mData[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val groupInfo = mData[groupPosition]
        val textView = if (convertView == null) {
            TextView(App.getContext())
        } else {
            convertView as TextView
        }
        textView.text = groupInfo.name
        textView.setTextColor(Color.BLACK)
        textView.textSize = 20.0f
        textView.setBackgroundColor(Color.GRAY)
        textView.setPadding(8, 8, 8, 8)
        return textView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return mData[groupPosition].list?.size ?: 0
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val childInfo = mData[groupPosition].list!![childPosition]
        val textView = if (convertView == null) {
            TextView(App.getContext())
        } else {
            convertView as TextView
        }
        textView.text = childInfo.number
        textView.setTextColor(Color.BLACK)
        textView.textSize = 20.0f
        textView.setBackgroundColor(Color.WHITE)
        textView.setPadding(8, 8, 8, 8)
        return textView
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return mData.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return mData[groupPosition].list!![childPosition]
    }
}