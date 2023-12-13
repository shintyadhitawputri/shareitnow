package com.dicoding.mysubmissionstory.view.camera

import androidx.lifecycle.ViewModel
import com.dicoding.mysubmissionstory.data.repository.RetrofitRepository
import java.io.File

class UploadViewModel(private val repository: RetrofitRepository) : ViewModel() {
    fun uploadImage(file: File, description: String) = repository.uploadImage(file, description)
}