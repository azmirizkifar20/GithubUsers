package org.marproject.githubuser.data.local.room

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.marproject.githubuser.data.local.entity.User

@Dao
interface UserDao {

    @Insert
    fun addFavorite(user: User): Long

    @Query("DELETE FROM user WHERE id = :id")
    fun deleteFavorite(id: Int): Int

    @Query("SELECT * FROM user WHERE id = :id")
    fun getFavorite(id: Int): Cursor

    @Query("SELECT * FROM user")
    fun getFavorites(): Cursor

}