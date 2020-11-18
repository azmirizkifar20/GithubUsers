package org.marproject.githubuser.data.local.sqlite

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "org.marproject.githubuser"
    const val SCHEME = "content"

    class UserColumns : BaseColumns {

        companion object {
            const val TABLE_NAME = "user"
            const val _ID = "_id"
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
    }
}