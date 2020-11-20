package org.marproject.githubuser.utils.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import org.marproject.githubuser.data.local.room.UserDao
import org.marproject.githubuser.data.local.room.UserDatabase
import org.marproject.githubuser.data.local.room.DatabaseContract.AUTHORITY
import org.marproject.githubuser.data.local.room.DatabaseContract.CONTENT_URI
import org.marproject.githubuser.data.local.room.DatabaseContract.TABLE_NAME
import org.marproject.githubuser.utils.helpers.MappingHelper.mapToObject
import kotlin.properties.Delegates

class FavoriteProvider : ContentProvider() {

    companion object {
        private const val FAVORITE = 1
        private const val FAVORITE_ID = 2
        private lateinit var userDao: UserDao
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            // content://org.marproject.githubuser/user
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVORITE)

            // content://org.marproject.githubuser/user/id
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", FAVORITE_ID)
        }

    }

    override fun onCreate(): Boolean {
        userDao = UserDatabase.getInstance(context as Context).userDao
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            FAVORITE -> userDao.getFavorites()
            FAVORITE_ID -> userDao.getFavorite(uri.lastPathSegment.toString().toInt())
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        var added by Delegates.notNull<Long>()

        values?.let {
            added = when(FAVORITE) {
                sUriMatcher.match(uri) -> userDao.addFavorite(values.mapToObject())
                else -> 0
            }
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (FAVORITE_ID) {
            sUriMatcher.match(uri) -> userDao.deleteFavorite(uri.lastPathSegment.toString().toInt())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }
}