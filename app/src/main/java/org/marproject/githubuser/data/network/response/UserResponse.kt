package org.marproject.githubuser.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserResponse (
    @field:SerializedName("id")
    val id: Int = 0,

    @field:SerializedName("name")
    val name: String = "",

    @field:SerializedName("login")
    val username: String = "",

    @field:SerializedName("html_url")
    val profile: String = "",

    @field:SerializedName("avatar_url")
    val avatar: String = "",

    @field:SerializedName("followers_url")
    val followers: String = "",

    @field:SerializedName("following_url")
    val following: String = ""
) : Parcelable