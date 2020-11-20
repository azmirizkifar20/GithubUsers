package org.marproject.githubuser.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.marproject.githubuser.data.network.service.ApiService
import org.marproject.githubuser.view.detail.DetailViewModel
import org.marproject.githubuser.view.detail.followers.FollowersViewModel
import org.marproject.githubuser.view.detail.following.FollowingViewModel
import org.marproject.githubuser.view.favorite.FavoriteViewModel
import org.marproject.githubuser.view.main.HomeViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(" https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()

        retrofit.create(ApiService::class.java)
    }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { FavoriteViewModel(get()) }
    viewModel { FollowersViewModel(get()) }
    viewModel { FollowingViewModel(get()) }
    viewModel { DetailViewModel(get(), get()) }
}