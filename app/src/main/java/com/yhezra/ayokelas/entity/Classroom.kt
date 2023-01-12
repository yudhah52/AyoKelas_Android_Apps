package com.yhezra.ayokelas.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Classroom(
    var id: Int = 0,
    var title: String? = null,
    var description: String? = null,
    var date: String? = null,
    var schedule: String? = null,
    var userName: String? = null,
    var userEmail: String? = null,
    var members: String? = null,
    var assignments: String? = null
) : Parcelable
