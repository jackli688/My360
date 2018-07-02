package com.guard.ui.adapters

import android.content.Context
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.RecyclerView
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.guard.R
import com.guard.persenter.ProcessManagerPresenterImp

class ProcessManagerAdapter(var context: Context, private var runningProcesses: ProcessManagerPresenterImp.ProcessListBean)
    : RecyclerView.Adapter<ProcessManagerAdapter.ProcessHolder>() {
    companion object {
        const val reminderItem = 1
        const val commonItem = 2
    }

    var mShowSystem: Boolean = false
    override fun getItemViewType(position: Int): Int {
        return if (runningProcesses.useProcessList.isEmpty()) {
            if (position == 0) {
                reminderItem
            } else {
                commonItem
            }
        } else {
            if (position == 0 || position == runningProcesses.useProcessList.size + 1) {
                reminderItem
            } else {
                commonItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ProcessHolder {
        val item = when (viewType) {
            reminderItem -> {
                LayoutInflater.from(context).inflate(R.layout.item_softmanagertitle, parent, false)
            }
            else -> {
                LayoutInflater.from(context).inflate(R.layout.item_processmanagercommon, parent, false)
            }
        }
        return ProcessHolder(item, viewType)
    }

    override fun getItemCount(): Int {
        if (mShowSystem) {
            if (runningProcesses.useProcessList.isEmpty())
                return runningProcesses.toatalSize + 1
            return runningProcesses.toatalSize + 2

        } else {
            return if (runningProcesses.useProcessList.isEmpty())
                0
            else {
                runningProcesses.useProcessList.size + 1
            }
        }

    }

    override fun onBindViewHolder(holder: ProcessHolder?, position: Int) {
        val useProcessList = runningProcesses.useProcessList
        val sysProcessList = runningProcesses.sysProcessList
        if (mShowSystem) {
            if (!useProcessList.isEmpty()) {
                when {
                    position == 0 -> {
                        holder?.remindTitle?.text = "三方进程"
                    }
                    0 < position && position < useProcessList.size + 1 -> {
                        val tempPosition = position - 1
                        holder?.icon?.setImageDrawable(useProcessList[tempPosition].icon)
                        holder?.labelName?.text = useProcessList[tempPosition].name
                        holder?.storeTitle?.text = "内存占用:"
                        holder?.storeSize?.text = Formatter.formatFileSize(context, useProcessList[tempPosition].memorySize)
                        holder?.selected?.isChecked = useProcessList[tempPosition].isChecked
                    }
                    position == useProcessList.size + 1 -> {
                        holder?.remindTitle?.text = "系统进程"
                    }
                    useProcessList.size + 1 < position && position < runningProcesses.toatalSize + 2 -> {
                        val tempPosition = position - useProcessList.size - 2
                        holder?.icon?.setImageDrawable(sysProcessList[tempPosition].icon)
                        holder?.labelName?.text = sysProcessList[tempPosition].name
                        holder?.storeTitle?.text = "内存占用:"
                        holder?.storeSize?.text = Formatter.formatFileSize(context, sysProcessList[tempPosition].memorySize)
                        holder?.selected?.isChecked = sysProcessList[tempPosition].isChecked
                    }
                }
            } else {
                when (position) {
                    0 -> {
                        holder?.remindTitle?.text = "系统进程"
                    }
                    else -> {
                        val tempPosition = position - 1
                        holder?.icon?.setImageDrawable(sysProcessList[tempPosition].icon)
                        holder?.labelName?.text = sysProcessList[tempPosition].name
                        holder?.storeTitle?.text = "内存占用:"
                        holder?.storeSize?.text = Formatter.formatFileSize(context, sysProcessList[tempPosition].memorySize)
                        holder?.selected?.isChecked = sysProcessList[tempPosition].isChecked
                    }
                }
            }

        } else {
            if (!useProcessList.isEmpty()) {
                when {
                    position == 0 -> {
                        holder?.remindTitle?.text = "三方进程"
                    }
                    0 < position && position < useProcessList.size + 1 -> {
                        val tempPosition = position - 1
                        holder?.icon?.setImageDrawable(useProcessList[tempPosition].icon)
                        holder?.labelName?.text = useProcessList[tempPosition].name
                        holder?.storeTitle?.text = "内存占用:"
                        holder?.storeSize?.text = Formatter.formatFileSize(context, useProcessList[tempPosition].memorySize)
                        holder?.selected?.isChecked = useProcessList[tempPosition].isChecked
                    }
                }
            }

        }
    }


    class ProcessHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
        var icon: ImageView? = null
        var labelName: TextView? = null
        var storeTitle: TextView? = null
        var storeSize: TextView? = null
        var selected: AppCompatCheckBox? = null
        var remindTitle: TextView? = null


        init {
            if (viewType == ProcessManagerAdapter.commonItem) {
                icon = itemView.findViewById(R.id.processManager_iv_appIcon) as ImageView
                labelName = itemView.findViewById(R.id.processManager_tv_appLabel) as TextView
                storeSize = itemView.findViewById(R.id.softManager_tv_appSize) as TextView
                selected = itemView.findViewById(R.id.processManager_cb_selected) as AppCompatCheckBox
            } else {
                remindTitle = itemView.findViewById(R.id.softManager_tv_title) as TextView
            }
        }
    }
}
