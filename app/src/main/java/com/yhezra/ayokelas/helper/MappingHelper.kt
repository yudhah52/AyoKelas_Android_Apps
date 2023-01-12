package com.yhezra.ayokelas.helper

import android.database.Cursor
import com.yhezra.ayokelas.db.DatabaseContract
import com.yhezra.ayokelas.entity.Classroom

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Classroom> {
        val classroomList = ArrayList<Classroom>()

        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.ClassroomColumns._ID))
                val title = getString(getColumnIndexOrThrow(DatabaseContract.ClassroomColumns.TITLE))
                val description = getString(getColumnIndexOrThrow(DatabaseContract.ClassroomColumns.DESCRIPTION))
                val date = getString(getColumnIndexOrThrow(DatabaseContract.ClassroomColumns.DATE))
                val schedule = getString(getColumnIndexOrThrow(DatabaseContract.ClassroomColumns.SCHEDULE))
                val userName = getString(getColumnIndexOrThrow(DatabaseContract.ClassroomColumns.USER_NAME))
                val userEmail = getString(getColumnIndexOrThrow(DatabaseContract.ClassroomColumns.USER_EMAIL))
                val members = getString(getColumnIndexOrThrow(DatabaseContract.ClassroomColumns.MEMBERS))
                val assignments = getString(getColumnIndexOrThrow(DatabaseContract.ClassroomColumns.ASSIGNMENTS))
                classroomList.add(Classroom(id, title, description, date, schedule, userName, userEmail, members, assignments))
            }
        }
        return classroomList
    }
}