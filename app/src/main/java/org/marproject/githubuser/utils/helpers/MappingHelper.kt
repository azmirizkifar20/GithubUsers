package org.marproject.githubuser.utils.helpers

import android.database.Cursor
import org.marproject.githubuser.data.local.entity.User
import org.marproject.githubuser.data.local.sqlite.DatabaseContract
import org.marproject.githubuser.data.network.response.UserResponse

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

    fun mapCursorToObject(userCursor: Cursor?): User {
        var user = User()

        userCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
            val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME))
            val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
            val profile = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.PROFILE))
            val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR))

            user = User(id, name, username, profile, avatar)
        }

        return user
    }

    fun UserResponse.mapRemoteModelToLocal(): User {
        return User(
            id = id,
            name = name,
            username = username,
            profile = profile,
            avatar = avatar
        )
    }
}