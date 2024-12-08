package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.rizka.submission1intermediate.loginwithanimation.databinding.ActivitySignupBinding
import com.rizka.submission1intermediate.view.login.LoginActivity
import com.rizka.submission1intermediate.view.signup.SignupViewModel

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        val factory = ViewModelFactory.getInstance(this)
        signupViewModel = ViewModelProvider(this, factory)[SignupViewModel::class.java]

        setupView()
        setupAction()
        observeViewModel()
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

            when {
                name.isEmpty() -> binding.nameEditTextLayout.error = "Nama tidak boleh kosong"
                email.isEmpty() -> binding.emailEditTextLayout.error = "Email tidak boleh kosong"
                password.isEmpty() -> binding.passwordEditTextLayout.error =
                    "Password tidak boleh kosong"

                password.length < 8 -> binding.passwordEditTextLayout.error =
                    "Password harus lebih dari 8 karakter"

                else -> {
                    signupViewModel.register()
                }
            }
        }
    }

    private fun observeViewModel() {
        signupViewModel.registrationResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result -> {
                    binding.progressBar.visibility = View.GONE
                    showAlert("Sukses", result.data.message ?: "Registrasi berhasil.")
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showAlert("Gagal", result.error)
                }
            }
        }

        // Observe LiveData for input validation
        signupViewModel.name.observe(this) { name ->
            binding.nameEditTextLayout.error =
                if (name.isEmpty()) "Nama tidak boleh kosong" else null
        }

        signupViewModel.email.observe(this) { email ->
            binding.emailEditTextLayout.error = when {
                email.isEmpty() -> "Email tidak boleh kosong"
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Email tidak valid"
                else -> null
            }
        }

        signupViewModel.password.observe(this) { password ->
            binding.passwordEditTextLayout.error = when {
                password.isEmpty() -> "Password tidak boleh kosong"
                password.length < 8 -> "Password harus lebih dari 8 karakter"
                else -> null
            }
        }
    }

    private fun showAlert(title: String, message: String, onPositive: (() -> Unit)? = null) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("OK") { _, _ -> onPositive?.invoke() }
            create()
            show()
        }
    }


}