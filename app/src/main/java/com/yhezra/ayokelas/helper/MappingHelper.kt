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
                val teacher = getString(getColumnIndexOrThrow(DatabaseContract.ClassroomColumns.TEACHER))
                val user = getString(getColumnIndexOrThrow(DatabaseContract.ClassroomColumns.USER))
                classroomList.add(Classroom(id, title, description, date, teacher, user))
            }
        }
        return classroomList
    }
}