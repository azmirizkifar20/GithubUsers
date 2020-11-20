package org.marproject.githubuser.utils.helpers

import android.content.ContentValues
import android.database.Cursor
import org.marproject.githubuser.data.local.entity.User
import org.marproject.githubuser.data.local.room.DatabaseContract.AVATAR
import org.marproject.githubuser.data.local.room.DatabaseContract.ID
import org.marproject.githubuser.data.local.room.DatabaseContract.NAME
import org.marproject.githubuser.data.local.room.DatabaseContract.PROFILE
import org.marproject.githubuser.data.local.room.DatabaseContract.USERNAME
import org.marproject.githubuser.data.network.response.UserResponse

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

    fun mapCursorToObject(userCursor: Cursor?): User {
        var user = User()

        userCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(ID))
            val name = getString(getColumnIndexOrThrow(NAME))
            val username = getString(getColumnIndexOrThrow(USERNAME))
            val profile = getString(getColumnIndexOrThrow(PROFILE))
            val avatar = getString(getColumnIndexOrThrow(AVATAR))

            user = User(id, name, username, profile, avatar)
        }

        return user
    }
    
    fun ContentValues.mapToObject(): User {
        return User(
            id = getAsInteger("id"),
            name = getAsString("name"),
            username = getAsString("username"),
            profile = getAsString("profile"),
            avatar = getAsString("avatar")
        )
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