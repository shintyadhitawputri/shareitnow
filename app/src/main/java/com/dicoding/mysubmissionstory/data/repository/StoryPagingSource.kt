package com.dicoding.mysubmissionstory.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.mysubmissionstory.data.preferences.UserModel
import com.dicoding.mysubmissionstory.data.preferences.UserPreference
import com.dicoding.mysubmissionstory.data.response.ListStoryItem
import com.dicoding.mysubmissionstory.data.retrofit.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class StoryPagingSource(private val apiService: ApiService, userPreference: UserPreference) : PagingSource<Int, ListStoryItem>()  {

    private val userModel: UserModel = runBlocking {
        userPreference.getSession().first()
    }
    private val token: String = userModel.token

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStories(page, params.loadSize, "Bearer $token")

            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.listStory.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}