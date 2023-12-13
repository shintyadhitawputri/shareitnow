package com.dicoding.mysubmissionstory.view.maps

import android.util.Log
import androidx.lifecycle.ViewModel
import com.dicoding.mysubmissionstory.data.repository.RetrofitRepository
import com.dicoding.mysubmissionstory.data.response.StoryResponse

class MapsViewModel(private val retrofitrepository: RetrofitRepository) : ViewModel() {
    suspend fun getStoriesWithLocation(location: Int): StoryResponse {
        try {
            val mapsDisplay = retrofitrepository.getStoriesWithLocation(location)
            Log.d("MapsViewModel", "Data dari server: ${mapsDisplay.listStory}")
            return mapsDisplay
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Terjadi kesalahan pada server."
            Log.e("MapsViewModel", "Proses pengambilan maps gagal: $errorMessage")
            throw e
        }
    }
}