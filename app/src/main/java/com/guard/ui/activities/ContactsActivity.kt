package com.guard.ui.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.guard.R
import com.guard.model.bean.Constants
import com.guard.model.utils.ItemClickSupport


class ContactsActivity : AppCompatActivity() {

    private var mContactsData: ArrayList<ContactBean> = ArrayList(100)
    private lateinit var mContactsView: RecyclerView
    private lateinit var contactsAdapter: ContactsAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        mContactsView = findViewById(R.id.contacts_rv_contacts) as RecyclerView
        progressBar = findViewById(R.id.contacts_pb_loading) as ProgressBar
        mContactsView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        contactsAdapter = ContactsAdapter(this)
        mContactsView.adapter = contactsAdapter
        val clickSupport = ItemClickSupport.addTo(mContactsView)
        clickSupport.setOnItemClickListener(object : ItemClickSupport.OnItemClickListener {
            override fun onItemClicked(recyclerview: RecyclerView, position: Int, v: View) {
                if (mContactsData.isNotEmpty()) {
                    val intent = Intent(this@ContactsActivity, SetUp3Activity::class.java)
                    intent.putExtra(Constants.SAFENUMBER, mContactsData[position].number)
                    setResult(Constants.SETUPASKSAFENUMBERREQUESTCODE, intent)
                    this@ContactsActivity.finish()
                }
            }
        })
        progressBar.visibility = VISIBLE
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //进行授权
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), 1)
        } else {
            Thread({
                val contacts = getContacts()
                runOnUiThread({
                    progressBar.visibility = GONE
                    mContactsData.addAll(contacts)
                    contactsAdapter.notifyDataSetChanged()
                })
            }).start()
        }

    }


    inner class ContactsAdapter(val context: Context) : RecyclerView.Adapter<ContactsAdapter.ContactsHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.item_contacts, null, false)
            return ContactsHolder(view)
        }

        override fun getItemCount(): Int {
            return mContactsData.size
        }

        override fun onBindViewHolder(holder: ContactsHolder, position: Int) {
            if (mContactsData.isNotEmpty()) {
                val contactBean = mContactsData[position]
                val bitmap = getContactPhoto(contactBean.contact_id)
                bitmap?.let { holder.icon.setImageBitmap(bitmap) }
                holder.name.text = contactBean.name
                holder.number.text = contactBean.number
            }
        }

        inner class ContactsHolder(v: View) : RecyclerView.ViewHolder(v) {
            var icon = v.findViewById(R.id.item_iv_icon) as ImageView
            var name = v.findViewById(R.id.item_tv_name) as TextView
            var number = v.findViewById(R.id.item_tv_number) as TextView
        }
    }

    inner class ContactBean(var name: String, var number: String, var contact_id: Int)


    private fun getContacts(): ArrayList<ContactsActivity.ContactBean> {
        val arrayList = ArrayList<ContactBean>()
        val contentResolver = this.contentResolver
        val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
        val cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null)
        while (cursor.moveToNext()) {
            val name = cursor.getString(0)
            val number = cursor.getString(1)
            val id = cursor.getInt(2)
            val contactBean = ContactBean(name, number, id)
            arrayList.add(contactBean)
        }
        cursor.close()
        return arrayList
    }

    private fun getContactPhoto(id: Int): Bitmap? {
        val contentResolver = this.contentResolver
        val uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, id.toString())
        val inputStream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()
        return bitmap
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && permissions.isNotEmpty()) {
            if (permissions[0] == Manifest.permission.READ_CONTACTS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Thread({
                    val contacts = getContacts()
                    runOnUiThread({
                        progressBar.visibility = GONE
                        mContactsData.addAll(contacts)
                        contactsAdapter.notifyDataSetChanged()
                    })
                }).start()
            }
        }
    }


    override fun onDestroy() {
        ItemClickSupport.removeFrom(mContactsView)
        super.onDestroy()
    }
}
