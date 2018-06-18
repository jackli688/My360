package com.guard.model.db

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.guard.App
import com.guard.model.bean.CommonNumberGroupInfo
import java.io.File

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.model.db
 * @description: description
 * @date: 2018/6/17
 * @time: 17:07
 */
class CommonNumberDao private constructor() {

    private var openDatabase: SQLiteDatabase? = null
    private val Tag = this.javaClass.simpleName

    init {
        Log.e(Tag, "init's block run")
        val filePath = "data/data/${App.getContext().packageName}/databases/commonnum.db"
        val datbaseFile = File(filePath)
        if (datbaseFile.exists()) {
            openDatabase = SQLiteDatabase.openDatabase(datbaseFile.absolutePath, null, SQLiteDatabase.OPEN_READONLY)
        }
    }

    companion object {
        val instances by lazy {
            CommonNumberDao()
        }
    }


    @SuppressLint("Recycle")
    fun queryGroup(): List<CommonNumberGroupInfo>? {
        if (openDatabase != null) {
            val cursour = openDatabase!!.query("classlist", arrayOf("name", "idx"), null, null, null, null, null)
            return if (cursour != null) {
                val list = ArrayList<CommonNumberGroupInfo>()
                while (cursour.moveToNext()) {
                    val name = cursour.getString(0)
                    val idx = cursour.getString(1)
                    val child = queryChild(idx)
                    list += CommonNumberGroupInfo(name = name, idx = idx, list = child)
                }
                cursour.close()
                openDatabase!!.close()
                list
            } else {
                null
            }
        } else {
            return null
        }
    }


    fun queryChild(idx: String): List<CommonNumberChildInfo>? {
        if (openDatabase != null) {
            val cursor = openDatabase!!.query("table$idx", arrayOf("number", "name"), null, null, null, null, null)
            return if (cursor != null) {
                val list = ArrayList<CommonNumberChildInfo>()
                while (cursor.moveToNext()) {
                    val number = cursor.getString(0)
                    val name = cursor.getString(1)
                    list += CommonNumberChildInfo(number = number, name = name)
                }
                cursor.close()
//                openDatabase!!.close()
                list
            } else {
                null
            }
        } else {
            return null
        }
    }
}