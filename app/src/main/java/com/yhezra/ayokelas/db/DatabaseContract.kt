package com.yhezra.ayokelas.db

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class ClassroomColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "classroom"
            const val _ID = "_id"
            const val TITLE = "title"
            const val DESCRIPTION = "description"
            const val DATE = "date"
            const val TEACHER = "teacher"
            const val USER = "user"
        }

    }
}