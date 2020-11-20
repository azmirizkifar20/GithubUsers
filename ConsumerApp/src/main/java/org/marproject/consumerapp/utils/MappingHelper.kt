package org.marproject.consumerapp.utils

import android.database.Cursor
import org.marproject.consumerapp.data.DatabaseContract
import org.marproject.consumerapp.data.User

object MappingHelper {

    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<User> {
        val userList = ArrayList<User>()

        userCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val profile = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.PROFILE))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR))

                userList.add(User(id, name, username, profile, avatar))
            }
        }

        return userList
    }

}