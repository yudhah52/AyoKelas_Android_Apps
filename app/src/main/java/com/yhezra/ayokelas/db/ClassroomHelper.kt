package com.yhezra.ayokelas.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.yhezra.ayokelas.db.DatabaseContract.ClassroomColumns.Companion.DATE
import com.yhezra.ayokelas.db.DatabaseContract.ClassroomColumns.Companion.DESCRIPTION
import com.yhezra.ayokelas.db.DatabaseContract.ClassroomColumns.Companion.TABLE_NAME
import com.yhezra.ayokelas.db.DatabaseContract.ClassroomColumns.Companion.TEACHER
import com.yhezra.ayokelas.db.DatabaseContract.ClassroomColumns.Companion.TITLE
import com.yhezra.ayokelas.db.DatabaseContract.ClassroomColumns.Companion.USER
import com.yhezra.ayokelas.db.DatabaseContract.ClassroomColumns.Companion._ID
import com.yhezra.ayokelas.entity.Classroom
import java.sql.SQLException

class ClassroomHelper(context: Context) {
    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME

        private var INSTANCE: ClassroomHelper? = null
        fun getInstance(context: Context): ClassroomHelper = INSTANCE ?: synchronized(this) {
            INSTANCE ?: ClassroomHelper(context)
        }
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    fun queryById(id:String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }

    fun getAllClassroom(): ArrayList<Classroom> {
        val arrayList = ArrayList<Classroom>()
        val cursor = database.query(DATABASE_TABLE, null, null, null, null, null,
            "$_ID ASC", null)
        cursor.moveToFirst()
        var classroom: Classroom
        if (cursor.count > 0) {
            do {
                classroom = Classroom()
                classroom.id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID))
                classroom.title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE))
                classroom.description = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION))
                classroom.date = cursor.getString(cursor.getColumnIndexOrThrow(DATE))
                classroom.teacher = cursor.getString(cursor.getColumnIndexOrThrow(TEACHER))
                classroom.user = cursor.getString(cursor.getColumnIndexOrThrow(USER))

                arrayList.add(classroom)
                cursor.moveToNext()

            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun insertClassroom(classroom: Classroom): Long {
        val args = ContentValues()
        args.put(TITLE, classroom.title)
        args.put(DESCRIPTION, classroom.description)
        args.put(DATE, classroom.date)
        args.put(TEACHER,classroom.teacher)
        args.put(USER,classroom.user)
        return database.insert(DATABASE_TABLE, null, args)
    }

    fun updateClassroom(classroom: Classroom): Int {
        val args = ContentValues()
        args.put(TITLE, classroom.title)
        args.put(DESCRIPTION, classroom.description)
        args.put(DATE, classroom.date)
        args.put(TEACHER, classroom.teacher)
        args.put(USER, classroom.user)
        return database.update(DATABASE_TABLE, args, _ID + "= '" + classroom.id + "'", null)
    }

    fun deleteClassroom(id: Int): Int {
        return database.delete(TABLE_NAME, "$_ID = '$id'", null)
    }
}