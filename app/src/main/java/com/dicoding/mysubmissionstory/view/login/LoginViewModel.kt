package com.dicoding.mysubmissionstory.view.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.mysubmissionstory.data.preferences.UserModel
import com.dicoding.mysubmissionstory.data.repository.RetrofitRepository
import com.dicoding.mysubmissionstory.data.response.ErrorResponse
import com.dicoding.mysubmissionstory.data.response.LoginResponse
import kotlinx.coroutines.launch
import com.google.gson.Gson
import retrofit2.HttpException


class LoginViewModel(private val retrofitRepository: RetrofitRepository
) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            retrofitRepository.saveSession(user)
        }
    }
    suspend fun login(email: String, password: String): LoginResponse {
        try {
            val response = retrofitRepository.login(email, password)
            Log.d("RegisterProcess", "Proses registrasi berhasil: $response")
            response.loginResult?.token?.let { token ->
                val user = UserModel(email, token, true)
                retrofitRepository.saveSession(user)
            }
            return response
        } catch (e: Exception) {
            val jsonInString = (e as HttpException).response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Log.e("RegisterProcess", "Proses registrasi gagal: $errorMessage")
            throw e
        }
    }
}