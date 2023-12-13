package com.dicoding.mysubmissionstory.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.mysubmissionstory.data.response.RegisterResponse
import com.dicoding.mysubmissionstory.databinding.ActivitySignupBinding
import com.dicoding.mysubmissionstory.view.ViewModelFactory
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignupActivity : AppCompatActivity() {

    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
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
        supportActionBar?.hide()
    }


    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            lifecycleScope.launch {
                try {
                    showLoading(true)
                    val successResponse = viewModel.register(name, email, password)
                    successResponse.message?.let { it1 -> showToast(it1) }

                    AlertDialog.Builder(this@SignupActivity).apply {
                        setTitle("Welcome $name")
                        setMessage("Your account has been created, welcome to the family!")
                        setPositiveButton("Continue") { _, _ ->
                            finish()
                        }
                        create()
                        show()
                    }
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                    errorResponse.message?.let { it1 -> showToast(it1) }
                } finally {
                    showLoading(false)
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 7000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()


        val titleSignup = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)

        val nameSignup = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditSignup = ObjectAnimator.ofFloat(binding.nameEditText, View.ALPHA, 1f).setDuration(100)

        val emailSignup = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditSignup = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(100)

        val passwordSignup = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditSignup = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(100)

        val signupButton = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)


        val together = AnimatorSet().apply {
            playTogether(signupButton)
        }

        AnimatorSet().apply {
            playSequentially(titleSignup, nameSignup, nameEditSignup, emailSignup, emailEditSignup, passwordSignup, passwordEditSignup, together)
            startDelay = 100
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}