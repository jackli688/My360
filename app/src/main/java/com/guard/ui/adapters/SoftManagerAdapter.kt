package com.guard.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.guard.R
import com.guard.model.utils.AppsUtils
import com.guard.ui.adapters.SoftManagerAdapter.SoftHolder

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.ui.adapters
 * @description: description
 * @date: 2018/6/19
 * @time: 20:55
 * result: AppsUtils.ApplicationList
 */
class SoftManagerAdapter(var context: Context, result: AppsUtils.ApplicationList) : RecyclerView.Adapter<SoftHolder>() {
    private var systemApps: List<AppsUtils.AppInfo> = result.systemApps
    private var userApps: List<AppsUtils.AppInfo> = result.userApps

    companion object {
        const val commonItem = 0
        const val reminderItem = 1

    }

    override fun getItemViewType(position: Int): Int {
        return if (userApps.isEmpty()) {
            commonItem
        } else {
            if (position == 0 || position == userApps.size + 1) {
                reminderItem
            } else {
                commonItem
            }
        }
    }

    /**
     * 查看源码可知：第二参数的Type是调用getItemViewType()所得到
     */
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SoftHolder {
        val layoutId = when (viewType) {
            commonItem -> {
                R.layout.item_softmanagercommon
            }
            reminderItem -> {
                R.layout.item_softmanagertitle
            }
            else -> {
                0
            }
        }
        if (layoutId == 0) {
            throw(object : RuntimeException("layout id is wrong,please check again!!!") {})
        } else {
            val item = LayoutInflater.from(context).inflate(layoutId, parent, false)
            return SoftHolder(item, viewType)
        }
    }


    override fun getItemCount(): Int {
        val systemSize = (systemApps.size)
        val userSize = (userApps.size)
        return if (userSize == 0) {
            systemSize + 1
        } else {
            systemSize + userSize + 2
        }
    }

    override fun onBindViewHolder(holder: SoftHolder?, position: Int) {
        if (userApps.isNotEmpty()) {
            when {
                position == 0 -> {
                    @SuppressLint("SetTextI18n")
                    holder?.headerTitle?.text = "用户程序(${userApps.size}个)"
                }
                0 < position && position < userApps.size + 1 -> {
                    bindData(holder, userApps[position - 1])
                }
                position == userApps.size + 1 -> {
                    @SuppressLint("SetTextI18n")
                    holder?.headerTitle?.text = "系统程序(${systemApps.size}个)"
                }
                userApps.size + 1 < position && position < userApps.size + systemApps.size + 2 -> {
                    bindData(holder, systemApps[position - userApps.size - 1])
                }
            }
        } else {
            //系统应用
            when {
                position == 0 -> {
                    @SuppressLint("SetTextI18n")
                    holder?.headerTitle?.text = "系统程序(${systemApps.size}个)"
                }
                0 < position && position < systemApps.size + 1 -> {
                    bindData(holder, systemApps[position - 1])
                }
            }
        }

    }

    private fun bindData(holder: SoftHolder?, appInfo: AppsUtils.AppInfo) {
        holder?.icon?.setImageDrawable(appInfo.icon)
        holder?.ladle?.text = appInfo.name
        holder?.storeTitle?.text = "内存大小"
        holder?.storeMsg?.text = Formatter.formatFileSize(context, appInfo.size)
    }


    inner class SoftHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {

        lateinit var icon: ImageView
        lateinit var ladle: TextView
        lateinit var storeTitle: TextView
        lateinit var storeMsg: TextView

        lateinit var headerTitle: TextView


        init {
            when (viewType) {
                commonItem -> {
                    icon = itemView.findViewById(R.id.softManager_iv_appIcon) as ImageView
                    ladle = itemView.findViewById(R.id.softManager_tv_appLabel) as TextView
                    storeTitle = itemView.findViewById(R.id.softManager_tv_storeTitle) as TextView
                    storeMsg = itemView.findViewById(R.id.softManager_tv_appSize) as TextView
                }
                reminderItem -> {
                    headerTitle = itemView.findViewById(R.id.softManager_tv_title) as TextView
                }

            }
        }
    }
}