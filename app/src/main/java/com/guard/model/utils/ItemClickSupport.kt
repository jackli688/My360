package com.guard.model.utils

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.OnChildAttachStateChangeListener
import android.view.View
import android.view.View.OnLongClickListener
import com.guard.R

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.model.utils
 * @description: description
 * @date: 2018/5/15
 * @time: 19:50
 */
class ItemClickSupport(view: RecyclerView) {
    private val mRecyclerView: RecyclerView = view
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null

    val mOnClickListener: View.OnClickListener = View.OnClickListener { v ->
        if (mOnItemClickListener != null) {
            val holder = mRecyclerView.getChildViewHolder(v)
            mOnItemClickListener?.onItemClicked(mRecyclerView, holder.adapterPosition, v)
        }
    }

    val mOnLongClickListener: OnLongClickListener = OnLongClickListener { v ->
        if (mOnItemLongClickListener != null) {
            val holder = mRecyclerView.getChildViewHolder(v)
            return@OnLongClickListener mOnItemLongClickListener?.onItemLongClicked(mRecyclerView, holder.adapterPosition, v) == true
        }
        false
    }

    private val mAttachListener: OnChildAttachStateChangeListener = object : OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View?) {
            if (mOnItemClickListener != null) {
                view?.setOnClickListener(mOnClickListener)
            }
            if (mOnItemLongClickListener != null) {
                view?.setOnLongClickListener(mOnLongClickListener)
            }
        }

        override fun onChildViewDetachedFromWindow(view: View?) {}
    }

    init {
        mRecyclerView.setTag(R.id.item_click_support, this)
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener)
    }


    companion object {
        fun addTo(view: RecyclerView): ItemClickSupport {
            val tag = view.getTag(R.id.item_click_support)
            val support = when (tag) {
                null -> ItemClickSupport(view)
                else -> tag as ItemClickSupport
            }
            return support
        }

        fun removeFrom(view: RecyclerView): ItemClickSupport? {
            val tag = view?.getTag(R.id.item_click_support)
            var support: ItemClickSupport? = null
            if (tag != null) {
                support = tag as ItemClickSupport
                support.detach(view)
            }
            return support
        }


    }

    fun setOnItemClickListener(listener: OnItemClickListener): ItemClickSupport {
        mOnItemClickListener = listener
        return this
    }

    fun setOniItemLongClickListner(listener: OnItemLongClickListener): ItemClickSupport {
        mOnItemLongClickListener = listener
        return this
    }

    private fun detach(view: RecyclerView) {
        view.removeOnChildAttachStateChangeListener(mAttachListener)
        view.setTag(R.id.item_click_support,null)
    }


    interface OnItemClickListener {
        fun onItemClicked(recyclerview: RecyclerView, position: Int, v: View)
    }

    interface OnItemLongClickListener {
        fun onItemLongClicked(recyclerview: RecyclerView, position: Int, v: View?): Boolean
    }


}