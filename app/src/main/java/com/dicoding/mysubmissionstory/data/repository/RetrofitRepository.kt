package com.dicoding.mysubmissionstory.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.mysubmissionstory.data.preferences.UserModel
import com.dicoding.mysubmissionstory.data.preferences.UserPreference
import com.dicoding.mysubmissionstory.data.response.DetailResponse
import com.dicoding.mysubmissionstory.data.response.FileUploadResponse
import com.dicoding.mysubmissionstory.data.response.ListStoryItem
import com.dicoding.mysubmissionstory.data.response.LoginResponse
import com.dicoding.mysubmissionstory.data.response.RegisterResponse
import com.dicoding.mysubmissionstory.data.response.StoryResponse
import com.dicoding.mysubmissionstory.data.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class RetrofitRepository(
    private val apiService: ApiService,
    private val userPreference: UserPreference
){

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 1
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, userPreference)
            }
        ).liveData
    }

    suspend fun getDetail(storyId: String): DetailResponse {
        val userModel: UserModel = userPreference.getSession().first()
        val token: String = userModel.token
        return apiService.getDetail(storyId, "Bearer $token")
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun uploadImage(imageFile: File, description: String) = liveData {
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())

        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        val userModel: UserModel = userPreference.getSession().first()
        val token: String = userModel.token
        try {
            val successResponse = apiService.uploadImage(multipartBody, requestBody, "Bearer $token")
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
            emit(errorResponse.message?.let { ResultState.Error(it) })
        }
    }

    suspend fun getStoriesWithLocation(location: Int): StoryResponse {
        val userModel: UserModel = userPreference.getSession().first()
        val token: String = userModel.token
        return apiService.getStoriesWithLocation(location, "Bearer $token")
    }

}