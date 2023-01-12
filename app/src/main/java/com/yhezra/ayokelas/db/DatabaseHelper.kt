package com.yhezra.ayokelas.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.yhezra.ayokelas.db.DatabaseContract.ClassroomColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "dbclassroomapp"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_CLASSROOM = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.ClassroomColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.ClassroomColumns.TITLE} TEXT NOT NULL," +
                " ${DatabaseContract.ClassroomColumns.DESCRIPTION} TEXT NOT NULL," +
                " ${DatabaseContract.ClassroomColumns.DATE} TEXT NOT NULL,"+
                " ${DatabaseContract.ClassroomColumns.TEACHER} TEXT NOT NULL,"+
                " ${DatabaseContract.ClassroomColumns.USER} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_CLASSROOM)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}