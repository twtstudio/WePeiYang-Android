package com.yookiely.lostfond2.search

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.yookiely.lostfond2.service.Utils.Companion.CONTENT
import com.yookiely.lostfond2.service.Utils.Companion.ID
import com.yookiely.lostfond2.service.Utils.Companion.TABLE_NAME
import org.jetbrains.anko.db.*

class HistoryRecordHelper(val context : Context) : ManagedSQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "HRDataBase"

        private var instance : HistoryRecordHelper? = null

        @Synchronized
        fun getInstance(context: Context) : HistoryRecordHelper{
            if(instance == null){
                instance = HistoryRecordHelper(context.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase?){
        db?.createTable(TABLE_NAME, true,
                Pair(ID, INTEGER + PRIMARY_KEY + AUTOINCREMENT),
                Pair(CONTENT, TEXT))
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropTable(TABLE_NAME, true)
        onCreate(db)
    }
}

val Context.database:  HistoryRecordHelper
    get() = HistoryRecordHelper.getInstance(applicationContext)
