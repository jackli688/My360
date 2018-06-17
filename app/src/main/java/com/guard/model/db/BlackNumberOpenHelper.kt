package com.guard.model.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.guard.ui.activities.HarassmentInterceptionActivity

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.model.db
 * @description: description
 * @date: 2018/5/30
 * @time: 23:24
 */
class BlackNumberOpenHelper : SQLiteOpenHelper {
    constructor(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : super(context, name, factory, version)

    companion object {
        val BLACKNUMBER_DBNAME = "blacknumber.db"
        val BLACKNUMBER_VERSION = 1
        val BLACKNUMBER_TABLENAME = "info"
        val BLACKNUMBER_ID = "_id"
        val BLACKNUMBER_BLACKNUMBER = "blacknumber"
        val BLACKNUMBER_MODE = "mode"
        val BLACKNUMBER_SQL =
                "create table $BLACKNUMBER_TABLENAME($BLACKNUMBER_ID integer primary key autoincrement,$BLACKNUMBER_BLACKNUMBER varchar(20),$BLACKNUMBER_MODE varchar(2))"
        val BLACKNUMBER_CALL = 0
        val BLACKNUMBER_SMS = 1
        var BLACKNUMBER_ALL = 2

    }

    internal constructor(context: Context?) : this(context, BLACKNUMBER_DBNAME, null, BLACKNUMBER_VERSION)

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(BLACKNUMBER_SQL)
        Log.e(HarassmentInterceptionActivity.TAG, "创建表的语句是:$BLACKNUMBER_SQL")
        Log.e(HarassmentInterceptionActivity.TAG, "database create execute")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }


}