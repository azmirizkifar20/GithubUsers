package org.marproject.githubuser.view.detail

import android.app.Application
import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import org.marproject.githubuser.data.local.entity.User
import org.marproject.githubuser.data.local.sqlite.DatabaseContract
import org.marproject.githubuser.data.local.sqlite.DatabaseContract.UserColumns.Companion.CONTENT_URI
import org.marproject.githubuser.data.network.response.UserResponse
import org.marproject.githubuser.data.network.service.ApiService
import org.marproject.githubuser.utils.helpers.MappingHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val apiService: ApiService, private val application: Application) : ViewModel() {

    // create job & coroutine scope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun fetchData(username: String): LiveData<UserResponse> {
        val user = MutableLiveData<UserResponse>()
        val client = apiService.getDetailUser(username)

        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { user.value = it }
                } else {
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure : ${t.message.toString()}")
            }
        })

        return user
    }

    fun getUser(uri: Uri): LiveData<User> {
        val user = MutableLiveData<User>()
        uiScope.launch {
            val deferredUser = async(Dispatchers.IO) {
                val cursor = application.contentResolver.query(uri, null, null, null, null)
                cursor?.let {
                    if (cursor.moveToFirst())
                        MappingHelper.mapCursorToObject(cursor)
                    else
                        User()
                }
            }

            user.value = deferredUser.await()
        }

        return user
    }

    fun insertFavorite(data: User) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val values = ContentValues()
                values.put(DatabaseContract.UserColumns._ID, data.id)
                values.put(DatabaseContract.UserColumns.NAME, data.name)
                values.put(DatabaseContract.UserColumns.USERNAME, data.username)
                values.put(DatabaseContract.UserColumns.PROFILE, data.profile)
                values.put(DatabaseContract.UserColumns.AVATAR, data.avatar)

                application.contentResolver.insert(CONTENT_URI, values)
            }
        }
    }

    fun deleteFavorite(uri: Uri) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                application.contentResolver.delete(uri, null, null)
            }
        }
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}