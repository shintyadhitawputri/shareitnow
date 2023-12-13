package com.dicoding.mysubmissionstory.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mysubmissionstory.R
import com.dicoding.mysubmissionstory.databinding.ActivityMainBinding
import com.dicoding.mysubmissionstory.view.ViewModelFactory
import com.dicoding.mysubmissionstory.view.adapter.LoadingStateAdapter
import com.dicoding.mysubmissionstory.view.adapter.StoryAdapter
import com.dicoding.mysubmissionstory.view.camera.UploadActivity
import com.dicoding.mysubmissionstory.view.login.LoginActivity
import com.dicoding.mysubmissionstory.view.maps.MapsActivity
import com.dicoding.mysubmissionstory.view.starting.StartingActivity
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private val adapter = StoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                setupView()
                setupAction()
            } else {
                startActivity(Intent(this, StartingActivity::class.java))
                finish()
            }
        }
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

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
    }

    private fun setupAction() {
        showLoading(true)
        lifecycleScope.launch {
            try {
            } catch (e: HttpException) {
                val errorMessage = e.message ?: "An error occurred on the server."
                if (e.code() == 401) {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    showToast("Login failed")
                    startActivity(intent)
                } else {
                    Log.e("MainViewModel", "Failed fetch story: $errorMessage")
                }
            } finally {
                showLoading(false)
                showToast("Welcome")
            }
        }

        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.storyResponseLiveData.observe(this) { pagingData ->
            Log.d("DATA SERVER", pagingData.toString())
            adapter.submitData(lifecycle, pagingData)
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuPreference -> {
                    viewModel.logout()
                    true
                }
                R.id.mapsMenu -> {
                    val intentMap = Intent(this@MainActivity, MapsActivity::class.java)
                    startActivity(intentMap)
                    true
                }
                else -> false
            }
        }

        binding.fab.setOnClickListener {
            val intentFab = Intent(this@MainActivity, UploadActivity::class.java)
            startActivity(intentFab)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}