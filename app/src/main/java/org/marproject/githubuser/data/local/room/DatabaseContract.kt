package org.marproject.githubuser.data.local.room

import android.net.Uri

object DatabaseContract {

    const val AUTHORITY = "org.marproject.githubuser"
    const val SCHEME = "content"
    const val DB_NAME = "user.db"
    const val TABLE_NAME = "user"

    const val ID = "id"
    const val NAME = "name"
    const val USERNAME = "username"
    const val PROFILE = "profile"
    const val AVATAR = "avatar"

    // untuk membuat URI content://org.marproject.githubuser/user
    val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
        .authority(AUTHORITY)
        .appendPath(TABLE_NAME)
        .build()
}