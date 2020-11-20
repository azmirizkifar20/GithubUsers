package org.marproject.githubuser.view.favorite

import android.app.Application
import android.database.ContentObserver
import android.os.Handler
import android.os.HandlerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import org.marproject.githubuser.data.local.entity.User
import org.marproject.githubuser.data.local.room.DatabaseContract.CONTENT_URI
import org.marproject.githubuser.utils.helpers.MappingHelper

class FavoriteViewModel(private val application: Application) : ViewModel() {

    // create job & coroutine scope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _users = MutableLiveData<List<User>>()
    val user: LiveData<List<User>> get() = _users

    init {
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                readUsers()
            }
        }

        // read user on start
        readUsers()

        application.contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
    }

    fun readUsers() = uiScope.launch {
        val deferredUsers = async(Dispatchers.IO) {
            val cursor = application.contentResolver?.query(
                CONTENT_URI,
                null,
                null,
                null,
                null
            )
            MappingHelper.mapCursorToArrayList(cursor)
        }

        val users = deferredUsers.await()
        if (users.size > 0)
            _users.value = users
        else
            _users.value = listOf()
    }
}