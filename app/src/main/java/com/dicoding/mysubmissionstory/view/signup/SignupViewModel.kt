package com.dicoding.mysubmissionstory.view.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import com.dicoding.mysubmissionstory.data.repository.RetrofitRepository
import com.dicoding.mysubmissionstory.data.response.RegisterResponse

class SignupViewModel(private val retrofitrepository: RetrofitRepository) : ViewModel() {
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        try {
            val response = retrofitrepository.register(name, email, password)
            Log.d("RegisterProcess", "Proses registrasi berhasil: $response")
            return response
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Terjadi kesalahan pada server."
            Log.e("RegisterProcess", "Proses registrasi gagal: $errorMessage")
            throw e
        }
    }
}