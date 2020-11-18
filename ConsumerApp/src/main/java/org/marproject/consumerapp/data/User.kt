package org.marproject.consumerapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id: Int = 0,
    var name: String = "",
    var username: String = "",
    var profile: String = "",
    var avatar: String = ""
) : Parcelable