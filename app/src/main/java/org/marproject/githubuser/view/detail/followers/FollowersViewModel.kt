package org.marproject.githubuser.view.detail.followers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.marproject.githubuser.data.network.response.UserResponse
import org.marproject.githubuser.data.network.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel(private val apiService: ApiService) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchData(username: String): LiveData<List<UserResponse>> {
        _isLoading.value = true
        val users = MutableLiveData<List<UserResponse>>()
        val client = apiService.getFollowersUser(username)

        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let { users.value = it }
                } else {
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure : ${t.message.toString()}")
            }
        })

        return users
    }

    companion object {
        private const val TAG = "FollowersViewModel"
    }
}