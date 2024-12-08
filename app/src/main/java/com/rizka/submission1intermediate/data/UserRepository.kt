package com.rizka.submission1intermediate.data

import com.google.gson.Gson
import com.rizka.submission1intermediate.api.ApiConfig
import com.rizka.submission1intermediate.api.ApiService
import com.rizka.submission1intermediate.data.pref.UserModel
import com.rizka.submission1intermediate.data.pref.UserPreference
import com.rizka.submission1intermediate.data.response.LoginResponse
import com.rizka.submission1intermediate.data.response.RegisterResponse
import com.rizka.submission1intermediate.data.response.StoryResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        try {
            return apiService.register(name, email, password)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            throw Exception(errorResponse.message ?: "Unknown error occurred")
        }
    }

    suspend fun login(email: String, password: String): LoginResponse {
        try {
            return apiService.login(email, password)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            throw Exception(errorResponse.message ?: "Unknown error occurred")
        }
    }


    suspend fun getStories(token: String, page: Int?, size: Int?, location: Int): StoryResponse {
        return ApiConfig.getApiService(token).getStories(page, size, location)
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun getStory(): StoryResponse {
        return apiService.getStories()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}