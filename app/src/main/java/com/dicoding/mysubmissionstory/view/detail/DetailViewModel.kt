package com.dicoding.mysubmissionstory.view.detail

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mysubmissionstory.data.repository.RetrofitRepository
import com.dicoding.mysubmissionstory.data.response.DetailResponse


class DetailViewModel(private val retrofitRepository: RetrofitRepository) : ViewModel() {

    private val detailResponseLiveData: MutableLiveData<DetailResponse> = MutableLiveData()
    fun getDetailResponseLiveData(): LiveData<DetailResponse> {
        return detailResponseLiveData
    }

    @SuppressLint("LongLogTag")
    suspend fun getDetail(storyId: String) {
        try {
            Log.d("DetailViewModel", "Mengambil detail cerita untuk storyId: $storyId")
            val response = retrofitRepository.getDetail(storyId)
            Log.d("DetailViewModel", "Berhasil mengambil detail cerita")
            detailResponseLiveData.value = response
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Terjadi kesalahan pada server."
            Log.e("DetailViewModel", "Proses pengambilan detail cerita gagal: $errorMessage")
        }
    }


}
