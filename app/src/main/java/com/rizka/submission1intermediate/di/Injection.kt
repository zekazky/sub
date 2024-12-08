package com.rizka.submission1intermediate.di

import android.content.Context
import com.rizka.submission1intermediate.api.ApiConfig
import com.rizka.submission1intermediate.api.ApiService
import com.rizka.submission1intermediate.data.UserRepository
import com.rizka.submission1intermediate.data.pref.UserPreference
import com.rizka.submission1intermediate.data.pref.dataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val token = runBlocking {
            pref.getSession().firstOrNull()?.token.orEmpty()
        }
        val apiService = ApiConfig.getApiService(token)
        return UserRepository.getInstance(pref, apiService)
    }
}