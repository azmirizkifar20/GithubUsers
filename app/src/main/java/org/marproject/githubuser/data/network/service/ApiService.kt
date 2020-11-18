package org.marproject.githubuser.data.network.service

import org.marproject.githubuser.BuildConfig
import org.marproject.githubuser.data.network.response.SearchResponse
import org.marproject.githubuser.data.network.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun searchUsers(
        @Query("q") search: String
    ) : Call<SearchResponse>

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getDetailUser(
        @Path("username") username: String
    ) : Call<UserResponse>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getFollowingUser(
        @Path("username") username: String
    ) : Call<List<UserResponse>>

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getFollowersUser(
        @Path("username") username: String
    ) : Call<List<UserResponse>>

}