package com.sriyank.cpdemo.data

import android.net.Uri
import android.provider.BaseColumns

class NationContract {
    companion object {
        val CONTENT_AUTHORITY = "com.sriyank.cpdemo.data.NationProvider"
        val BASE_CONTENT_URI = Uri.parse("content://${CONTENT_AUTHORITY}")
        val PATH_COUNTRIES = "countries"
        class NationEntry : BaseColumns{
            companion object {
                val CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_COUNTRIES)
                val TABLE_NAME = "countries"

                val _ID = BaseColumns._ID
                val COLUMN_COUNTRY = "country"
                val COLUMN_CONTINENT = "continent"
            }
        }
    }
}