package com.sriyank.cpdemo.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.sriyank.cpdemo.data.NationContract.Companion.NationEntry;

class NationDbHelper(val context: Context): SQLiteOpenHelper(context,"nations.db",null,1) {

    private val SQL_CREATE_COUNTRY_TABLE = "CREATE TABLE ${NationEntry.TABLE_NAME}" +
            "(${NationEntry._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
            "${NationEntry.COLUMN_COUNTRY} TEXT NOT NULL," +
            "${NationEntry.COLUMN_CONTINENT} TEXT);"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_COUNTRY_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${NationEntry.TABLE_NAME}")
        onCreate(db)
    }
}