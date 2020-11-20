package org.marproject.githubuser.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import org.marproject.githubuser.data.local.room.DatabaseContract.AVATAR
import org.marproject.githubuser.data.local.room.DatabaseContract.ID
import org.marproject.githubuser.data.local.room.DatabaseContract.NAME
import org.marproject.githubuser.data.local.room.DatabaseContract.PROFILE
import org.marproject.githubuser.data.local.room.DatabaseContract.TABLE_NAME
import org.marproject.githubuser.data.local.room.DatabaseContract.USERNAME

@Entity(tableName = TABLE_NAME)
@Parcelize
data class User(
    @PrimaryKey
    @ColumnInfo(name = ID)
    var id: Int = 0,

    @ColumnInfo(name = NAME)
    var name: String = "",

    @ColumnInfo(name = USERNAME)
    var username: String = "",

    @ColumnInfo(name = PROFILE)
    var profile: String = "",

    @ColumnInfo(name = AVATAR)
    var avatar: String = ""
) : Parcelable