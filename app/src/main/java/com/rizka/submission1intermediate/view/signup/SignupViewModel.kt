package com.rizka.submission1intermediate.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.rizka.submission1intermediate.data.UserRepository
import com.rizka.submission1intermediate.data.pref.UserModel
import com.rizka.submission1intermediate.data.response.ErrorResponse
import com.rizka.submission1intermediate.data.response.LoginResponse
import com.rizka.submission1intermediate.data.response.RegisterResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignupViewModel(private val repository: UserRepository) : ViewModel() {

    // LiveData for user input
    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    // LiveData for registration result
    private val _registrationResult = MutableLiveData<RegisterResponse?>()
    val registrationResult: LiveData<RegisterResponse?> get() = _registrationResult

    fun register() {
        val userName = name.value
        val userEmail = email.value
        val userPassword = password.value

        if (userName != null && userEmail != null && userPassword != null) {
            viewModelScope.launch {
                try {
                    val response = repository.register(userName, userEmail, userPassword)
                    _registrationResult.value = response
                } catch (e: Exception) {
                    // Handle error
                    _registrationResult.value = null
                }
            }
        } else {
            // Handle empty fields if necessary
            _registrationResult.value = null
        }
    }
}