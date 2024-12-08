package com.rizka.submission1intermediate.view.main

import android.util.Log
import androidx.datastore.dataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.rizka.submission1intermediate.api.ApiConfig
import com.rizka.submission1intermediate.data.UserRepository
import com.rizka.submission1intermediate.data.pref.UserModel
import com.rizka.submission1intermediate.data.response.ErrorResponse
import com.rizka.submission1intermediate.data.response.ListStoryItem
import com.rizka.submission1intermediate.data.response.LoginResponse
import com.rizka.submission1intermediate.data.response.StoryResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response


class MainViewModel(private val repository: UserRepository) : ViewModel() {

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }


}