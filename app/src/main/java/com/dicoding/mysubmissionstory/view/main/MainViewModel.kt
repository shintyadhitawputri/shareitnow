package com.dicoding.mysubmissionstory.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.mysubmissionstory.data.preferences.UserModel
import com.dicoding.mysubmissionstory.data.repository.RetrofitRepository
import com.dicoding.mysubmissionstory.data.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val retrofitRepository: RetrofitRepository ) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return retrofitRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            retrofitRepository.logout()
        }
    }

    val storyResponseLiveData: LiveData<PagingData<ListStoryItem>> = retrofitRepository.getStories().cachedIn(viewModelScope)
}