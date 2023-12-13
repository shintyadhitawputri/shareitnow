package com.dicoding.mysubmissionstory.view

import com.dicoding.mysubmissionstory.view.detail.DetailViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mysubmissionstory.data.repository.RetrofitRepository
import com.dicoding.mysubmissionstory.di.Injection
import com.dicoding.mysubmissionstory.view.camera.UploadViewModel
import com.dicoding.mysubmissionstory.view.login.LoginViewModel
import com.dicoding.mysubmissionstory.view.main.MainViewModel
import com.dicoding.mysubmissionstory.view.maps.MapsViewModel
import com.dicoding.mysubmissionstory.view.signup.SignupViewModel

class ViewModelFactory(
    private val retrofitRepository: RetrofitRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(retrofitRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(retrofitRepository) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(retrofitRepository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(retrofitRepository) as T
            }
            modelClass.isAssignableFrom(UploadViewModel::class.java) -> {
                UploadViewModel(retrofitRepository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(retrofitRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    val retrofitRepository = Injection.provideRetrofitRepository(context)
                    INSTANCE = ViewModelFactory(retrofitRepository)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}
