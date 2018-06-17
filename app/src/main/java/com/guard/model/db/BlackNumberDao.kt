package com.guard.model.db

import android.content.ContentValues
import android.content.Context
import android.os.SystemClock
import com.guard.App
import com.guard.model.bean.BlackNumberInfo

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.model.db
 * @description: description
 * @date: 2018/5/30
 * @time: 23:51
 */
class BlackNumberDao private constructor(context: Context) {
    var blackNumberOpenHelper: BlackNumberOpenHelper = BlackNumberOpenHelper(context)

    private constructor() : this(App.getContext())

    companion object {
        val instance: BlackNumberDao by lazy { BlackNumberDao() }
    }

    /**
     *@Description:添加数据
     *@param:黑名单号码，拦截类型
     *@data:
     */
    @Synchronized
    fun add(blacknumber: String, mode: Int): Boolean {
        //1.获取数据库
        val database = blackNumberOpenHelper.writableDatabase
        //添加数据
        val values = ContentValues()
        values.put(BlackNumberOpenHelper.BLACKNUMBER_BLACKNUMBER, blacknumber)
        values.put(BlackNumberOpenHelper.BLACKNUMBER_MODE, mode)
        val insert = database.insert(BlackNumberOpenHelper.BLACKNUMBER_TABLENAME, null, values)
        database.close()
        return insert != (-1).toLong()
    }

    @Synchronized
    fun delete(blacknumber: String): Boolean {
        //1.获取数据库
        val database = blackNumberOpenHelper.writableDatabase
        val delete = database.delete(BlackNumberOpenHelper.BLACKNUMBER_TABLENAME,
                BlackNumberOpenHelper.BLACKNUMBER_BLACKNUMBER + "=?", arrayOf(blacknumber))
        //关闭数据库
        database.close()
        return delete != -1
    }

    /**
     *@Descrition:更新
     * @param:拦截模式，根据黑名单好吗更新拦截模式
     */
    @Synchronized
    fun updata(blacknumber: String, mode: Int): Boolean {
        val database = blackNumberOpenHelper.writableDatabase
        val values = ContentValues()
        values.put(BlackNumberOpenHelper.BLACKNUMBER_MODE, mode)
        val update = database.update(BlackNumberOpenHelper.BLACKNUMBER_TABLENAME,
                values, BlackNumberOpenHelper.BLACKNUMBER_BLACKNUMBER + "=?", arrayOf(blacknumber))
        database.close()
        return update != -1
    }


    @Synchronized
    fun query(blacknumber: String): Int {
        var mode: Int = -1
        val database = blackNumberOpenHelper.readableDatabase
        val cursor = database.query(BlackNumberOpenHelper.BLACKNUMBER_TABLENAME, arrayOf(BlackNumberOpenHelper.BLACKNUMBER_MODE),
                BlackNumberOpenHelper.BLACKNUMBER_BLACKNUMBER + "=?", arrayOf(blacknumber), null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            mode = cursor.getInt(0)
            cursor.close()
        }
        database.close()
        return mode
    }

    /**
     * question:https://blog.csdn.net/zx422359126/article/details/75944216
     */
    @Synchronized
    fun queryAll(): ArrayList<BlackNumberInfo> {
        val list = ArrayList<BlackNumberInfo>()
        val readableDatabase = blackNumberOpenHelper.readableDatabase
        val cursor = readableDatabase.query(BlackNumberOpenHelper.BLACKNUMBER_TABLENAME,
                arrayOf(BlackNumberOpenHelper.BLACKNUMBER_BLACKNUMBER, BlackNumberOpenHelper.BLACKNUMBER_MODE),
                null, null, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val blacknumber = cursor.getString(0)
                val mode = cursor.getInt(1)
                val blackNumberInfo = BlackNumberInfo(blacknumber, mode)
                list.add(blackNumberInfo)
            }
        }
        cursor.close()
        readableDatabase.close()
        return list
    }

    @Synchronized
    fun queryPat(maxNum: Int, startIndex: Int): ArrayList<BlackNumberInfo> {
        SystemClock.sleep(3000)
        val arrayList = ArrayList<BlackNumberInfo>(maxNum)
        val readableDatabase = blackNumberOpenHelper.readableDatabase
        val rawQuery = readableDatabase.rawQuery("select blacknumber,mode from info order by _id desc limit ? offset ?", arrayOf(maxNum.toString(), startIndex.toString()))
        if (rawQuery != null) {
            while (rawQuery.moveToNext()) {
                val blackNumber = rawQuery.getString(0)
                val mode = rawQuery.getInt(1)
                val blackNumberInfo = BlackNumberInfo(blackNumber, mode)
                arrayList.add(blackNumberInfo)
            }
        }
        rawQuery.close()
        readableDatabase.close()
        return arrayList
    }

}