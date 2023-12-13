package com.dicoding.mysubmissionstory.di

import android.content.Context
import com.dicoding.mysubmissionstory.data.preferences.UserPreference
import com.dicoding.mysubmissionstory.data.preferences.dataStore
import com.dicoding.mysubmissionstory.data.repository.RetrofitRepository
import com.dicoding.mysubmissionstory.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideRetrofitRepository(context: Context): RetrofitRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService()
        return RetrofitRepository(apiService, pref)
    }
}