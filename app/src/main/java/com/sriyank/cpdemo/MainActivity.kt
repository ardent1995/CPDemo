package com.sriyank.cpdemo

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.sriyank.cpdemo.data.NationContract.Companion.NationEntry
import com.sriyank.cpdemo.data.NationDbHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnInsert.setOnClickListener(this)
        btnUpdate.setOnClickListener(this)
        btnDelete.setOnClickListener(this)
        btnQueryRowById.setOnClickListener(this)
        btnDisplayAll.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnInsert -> insert()
            R.id.btnUpdate -> update()
            R.id.btnDelete -> delete()
            R.id.btnQueryRowById -> queryRowById()
            R.id.btnDisplayAll -> queryAndDisplayAll()
        }
    }

    fun insert() {
        val contentValues = ContentValues()
        contentValues.put(NationEntry.COLUMN_COUNTRY, etCountry.text.toString())
        contentValues.put(NationEntry.COLUMN_CONTINENT, etContinent.text.toString())
        val uriRowInserted = contentResolver.insert(NationEntry.CONTENT_URI,contentValues)
        Log.i(TAG,"items inserted at : ${uriRowInserted}")
    }

    fun update() {
        val contentValues = ContentValues()
        contentValues.put(NationEntry.COLUMN_CONTINENT, etNewContinent.text.toString())
        val selection = NationEntry.COLUMN_COUNTRY+" = ?"
        val selectionArgs = arrayOf(etWhereToUpdate.text.toString())
        val rowsUpdated = contentResolver.update(NationEntry.CONTENT_URI,contentValues,selection,selectionArgs)
        Log.i(TAG, "Number of rows updated : ${rowsUpdated}")
    }

    fun delete() {
        val selection = NationEntry.COLUMN_COUNTRY+" = ?"
        val selectionArgs = arrayOf(etWhereToDelete.text.toString())
        val uri = Uri.withAppendedPath(NationEntry.CONTENT_URI,etWhereToDelete.text.toString())
        val rowsDeleted = contentResolver.delete(uri,selection,selectionArgs)
        Log.i(TAG," Number of rows deleted : ${rowsDeleted}")
    }

    fun queryRowById() {
        val projection = arrayOf(NationEntry._ID,NationEntry.COLUMN_COUNTRY,NationEntry.COLUMN_CONTINENT)
        val selection = NationEntry._ID+" = ?"
        val selectionArgs = arrayOf(etQueryRowById.text.toString())
        val uri = Uri.withAppendedPath(NationEntry.CONTENT_URI,etQueryRowById.text.toString())
        val cursor = contentResolver.query(uri,projection,selection,selectionArgs,null)
        var str = ""
        while (cursor.moveToNext()){
            val columns = cursor.columnNames
            for (column in columns){
                str += "\t${cursor.getString(cursor.getColumnIndex(column))}"
            }

            str +="\n"
        }

        cursor.close()

        Log.i(TAG,str)
    }

    fun queryAndDisplayAll() {
        val intent = Intent(this,NationListActivity::class.java)
        startActivity(intent)
        /*val projection = arrayOf(NationEntry._ID,NationEntry.COLUMN_COUNTRY,NationEntry.COLUMN_CONTINENT)
        val cursor = contentResolver.query(NationEntry.CONTENT_URI,projection,null,null,null)
        var str = ""
        while (cursor.moveToNext()){
            val columns = cursor.columnNames
            for (column in columns){
                str += "\t${cursor.getString(cursor.getColumnIndex(column))}"
            }

            str +="\n"
        }

        cursor.close()

        Log.i(TAG,str)*/
    }
}
