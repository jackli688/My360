package com.guard.ui.customwidgets

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.guard.R
import com.guard.R.id.addressdialog_lv_bgs
import com.guard.model.bean.Constants
import com.guard.model.utils.SharePreferencesUtils
import kotlinx.android.synthetic.main.address_dialog.*

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.ui.customwidgets
 * @description: description
 * @date: 2018/6/7
 * @time: 17:26
 */
class AddressDialog : Dialog {

    internal constructor(context: Context?) : super(context, R.style.AddressDialog)

    private val titles: Array<String> = arrayOf("半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿")
    private val icons: Array<Int> = arrayOf(R.drawable.toast_address_normal,
            R.drawable.toast_address_orange, R.drawable.toast_address_blue,
            R.drawable.toast_address_gray, R.drawable.toast_address_green)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.address_dialog)
        window.attributes.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        val mListView = findViewById(R.id.addressdialog_lv_bgs) as ListView
         mListView.adapter = ColorStyleAdapter()
        mListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            SharePreferencesUtils.setInt(Constants.SPFILEA, Constants.ADDRESSDIALOGBG, icons[position])
            dismiss()
        }
    }

    inner class ColorStyleAdapter : BaseAdapter() {

        override fun getItem(position: Int): Any {
            return icons[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return icons.size
        }

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val mRootView = LayoutInflater.from(context).inflate(R.layout.item_addressdialog, null, false)
            val mIcon = mRootView.findViewById(R.id.addressdialogitem_iv_icon) as ImageView
            val mText = mRootView.findViewById(R.id.addressdialog_tv_text) as TextView
            val mSelectd = mRootView.findViewById(R.id.addressdialogitem_iv_select) as ImageView
            val drawableID = SharePreferencesUtils.getInt(Constants.SPFILEA, Constants.ADDRESSDIALOGBG, R.drawable.toast_address_orange)
            if (icons[position] == drawableID) {
                mSelectd.visibility = View.VISIBLE
            } else {
                mSelectd.visibility = View.GONE
            }

            mIcon.setImageResource(icons[position])
            mText.text = titles[position]
            return mRootView
        }


    }

}