package com.sriyank.cpdemo.data

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.sriyank.cpdemo.data.NationContract.Companion.NationEntry
import com.sriyank.cpdemo.data.NationContract.Companion.CONTENT_AUTHORITY
import com.sriyank.cpdemo.data.NationContract.Companion.PATH_COUNTRIES

class NationProvider : ContentProvider() {
    companion object {
        val TAG = NationProvider::class.java.simpleName
        val COUNTRIES = 1
        val COUNTRIES_COUNTRY_NAME = 2
        val COUNTRIES_ID = 3
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        init {
            uriMatcher.addURI(CONTENT_AUTHORITY, PATH_COUNTRIES, COUNTRIES)
            uriMatcher.addURI(CONTENT_AUTHORITY, PATH_COUNTRIES + "/#", COUNTRIES_ID)
            uriMatcher.addURI(CONTENT_AUTHORITY, PATH_COUNTRIES + "/*", COUNTRIES_COUNTRY_NAME)
        }
    }

    lateinit var databaseHelper: NationDbHelper

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when(uriMatcher.match(uri)){
            COUNTRIES_COUNTRY_NAME -> {
                val selection = NationEntry.COLUMN_COUNTRY +"= ?"
                val selectionArgs = arrayOf(uri.lastPathSegment.toString())
                return deleteRecord(NationEntry.TABLE_NAME,selection,selectionArgs)
            }
            else -> throw IllegalArgumentException(TAG +" Insert unknown URI: "+ uri)
        }
    }

    fun deleteRecord(tableName: String,selection: String?,selectionArgs: Array<String>?): Int{
        val database = databaseHelper.writableDatabase
        val rowsDeleted = database.delete(tableName,selection,selectionArgs)
        return rowsDeleted
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues): Uri? {
        when(uriMatcher.match(uri)){
            COUNTRIES -> return insertRecord(uri,values,NationEntry.TABLE_NAME)
                else -> throw IllegalArgumentException(TAG +" Insert unknown URI: "+ uri)
        }
    }

    private fun insertRecord(uri: Uri, values: ContentValues, tableName: String): Uri? {
        val database = databaseHelper.writableDatabase
        val rowId = database.insert(tableName,null,values)
         if(rowId == -1.toLong()){
             Log.i(TAG, "Insert error for URI "+ uri)
             return null
         }else{
             return ContentUris.withAppendedId(uri,rowId)
         }
    }

    override fun onCreate(): Boolean {
        databaseHelper = NationDbHelper(context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val database = databaseHelper.readableDatabase
        var cursor : Cursor? = null
        when(uriMatcher.match(uri)){
            COUNTRIES -> cursor = database.query(NationEntry.TABLE_NAME,projection,null,null,null,null,sortOrder)
            COUNTRIES_ID -> {
                val selection = NationEntry._ID+ " = ?"
                val selectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                cursor = database.query(
                    NationEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                )
            }
            else -> throw IllegalArgumentException(TAG + " Insert unknown URI : "+uri)
        }
        return cursor
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        when(uriMatcher.match(uri)){
            COUNTRIES -> return updateRecord(NationEntry.TABLE_NAME,values,selection,selectionArgs)
            else -> throw IllegalArgumentException(TAG +" Insert unknown URI : "+uri)
        }
    }

    fun updateRecord(tableName: String,contentValues: ContentValues?, selection: String?,selectionArgs: Array<String>?): Int{
        val database = databaseHelper.writableDatabase
        val rowUpdated = database.update(tableName,contentValues,selection,selectionArgs)
        return rowUpdated
    }
}
