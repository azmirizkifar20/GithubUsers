package org.marproject.consumerapp.utils

import android.database.Cursor
import org.marproject.consumerapp.data.DatabaseContract.AVATAR
import org.marproject.consumerapp.data.DatabaseContract.ID
import org.marproject.consumerapp.data.DatabaseContract.NAME
import org.marproject.consumerapp.data.DatabaseContract.PROFILE
import org.marproject.consumerapp.data.DatabaseContract.USERNAME
import org.marproject.consumerapp.data.User

object MappingHelper {

    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<User> {
        val userList = ArrayList<User>()

        userCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(ID))
                val name = getString(getColumnIndexOrThrow(NAME))
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val profile = getString(getColumnIndexOrThrow(PROFILE))
                val avatar = getString(getColumnIndexOrThrow(AVATAR))

                userList.add(User(id, name, username, profile, avatar))
            }
        }

        return userList
    }

}