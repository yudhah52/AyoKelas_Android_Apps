package com.yhezra.ayokelas.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Classroom(
    var id: Int = 0,
    var title: String? = null,
    var description: String? = null,
    var date: String? = null,
    var teacher: String? = null,
    var user: String? = null
//    var student: ArrayList<String>
) : Parcelable
