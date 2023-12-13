package com.dicoding.mysubmissionstory.view.detail

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dicoding.mysubmissionstory.databinding.ActivityDetailBinding
import com.dicoding.mysubmissionstory.view.ViewModelFactory
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailActivity : AppCompatActivity() {
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun setupAction() {
        val dataStoryId = intent.getStringExtra("key_id")
        dataStoryId?.let { Log.e("Ini key_id", it) }
        lifecycleScope.launch {
            try {
                dataStoryId?.let { viewModel.getDetail(dataStoryId) }
            } catch (e: HttpException) {
                val errorMessage = e.message ?: "Terjadi kesalahan pada server."
                Log.e("MainViewModel", "Gagal mengambil daftar cerita: $errorMessage")
                showToast("Failed to Show Story")
            } finally {
                viewModel.getDetailResponseLiveData().observe(this@DetailActivity) { detailResponse ->
                    if (detailResponse != null) {
                        binding.tvItemTitle.text = detailResponse.story!!.name
                        binding.tvItemDescription.text = detailResponse.story.description
                        Glide.with(this@DetailActivity)
                            .load(detailResponse.story.photoUrl)
                            .into(binding.imgItemPhoto)
                    }
                }
            }
        }
    }

    @Suppress("SameParameterValue")
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

