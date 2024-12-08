package com.rizka.submission1intermediate.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.rizka.submission1intermediate.data.UserRepository
import com.rizka.submission1intermediate.data.pref.UserModel
import com.rizka.submission1intermediate.data.pref.UserPreference
import com.rizka.submission1intermediate.data.response.ErrorResponse
import com.rizka.submission1intermediate.data.response.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                //get success message
                val message = repository.login(email, password).message
            } catch (e: HttpException) {
                //get error message
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
            }
        }
    }
}
