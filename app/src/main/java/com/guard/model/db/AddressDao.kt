package com.guard.model.db

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteDatabase
import android.util.Log.i
import com.guard.App
import java.io.File

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.model.db
 * @description: description
 * @date: 2018/6/5
 * @time: 10:38
 */
class AddressDao private constructor() {

    companion object {
        val instance: AddressDao by lazy { AddressDao() }
    }


    @Synchronized
    fun query(phone: String): String? {
//        val file = File("/data/data/${App.getContext().packageName}/databases", "address.db")
        val file = File(App.getContext().filesDir, "address.db")
        return if (!file.exists()) {
            null
        } else {
            var location: String? = null
            val openDatabase = SQLiteDatabase.openDatabase(file.absolutePath, null, SQLiteDatabase.OPEN_READONLY)
            if (phone.matches("^1[34578]\\d{9}$".toRegex())) {
//            val cursor = openDatabase.rawQuery("select location from data2 where id=(select outkey from data1 where id=?)", arrayOf(phone.substring(0, 7)))
                val cursor = openDatabase
                        .rawQuery(
                                "select location from data2 where id=(select outkey from data1 where id=?)",
                                arrayOf(phone.substring(0, 7)))
                cursor?.moveToNext()
                location = cursor.getString(0)
                cursor?.close()
                openDatabase.close()
                location
            } else {
                when (phone.length) {
                    3 -> {
                        location = "报警电话"
                    }
                    4 -> {
                        location = "模拟电话"
                    }
                    5 -> {
                        location = "客服电话"
                    }
                    6 -> {
                        location = "火星电话"
                    }
                    7 -> {
                        location = "座机电话"
                    }
                    8 -> {
                        location = "大城市座机电话"
                    }
                    else -> {
                        if (phone.length >= 10 && phone.startsWith("0")) {
                            var cursor = openDatabase.rawQuery("select location from data2 where area=?", arrayOf(phone.substring(1, 3)))
                            if (cursor.moveToNext()) {
                                location = cursor.getString(0)
                                location = location.substring(0, location.length - 2)
                                cursor.close()
                            } else {
                                cursor = openDatabase.rawQuery("select location from data2 where area=?", arrayOf(phone.substring(1, 4)))
                                if (cursor.moveToNext()) {
                                    location = cursor.getString(0)
                                    location = location.substring(0, location.length - 2)
                                    cursor.close()
                                }
                            }
                            openDatabase.close()
                        }
                    }
                }
            }
            return location
        }
    }


}