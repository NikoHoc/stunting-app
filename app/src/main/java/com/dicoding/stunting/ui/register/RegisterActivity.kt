package com.dicoding.stunting.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.stunting.data.remote.Result
import com.dicoding.stunting.R
import com.dicoding.stunting.databinding.ActivityRegisterBinding
import com.dicoding.stunting.ui.ViewModelFactory


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel  by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = resources.getColor(R.color.mint_green, theme)
        supportActionBar?.hide()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupAction()
        playAnimation()
    }


    private fun setupAction() {
        binding.edRegisterUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkFieldsForErrors()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edRegisterEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkFieldsForErrors()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkFieldsForErrors()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnRegister.setOnClickListener {
            val username = binding.edRegisterUsername.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            registerViewModel.registerUser(username, email, password).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressIndicator.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            val registerResponse = result.data
                            AlertDialog.Builder(this).apply {
                                setTitle(R.string.signup_success_alert_title)
                                setMessage(registerResponse.message)
                                setPositiveButton(R.string.success_alert_reply) { _, _ ->
                                    finish()
                                }
                                create()
                                show()
                            }
                            binding.progressIndicator.visibility = View.GONE
                        }
                        is Result.Error -> {
                            AlertDialog.Builder(this).apply {
                                setTitle(R.string.signup_error_alert_title)
                                setMessage(result.error)
                                setPositiveButton(R.string.error_alert_reply) { dialog, _ ->
                                    dialog.dismiss()
                                }
                                create()
                                show()
                            }
                            binding.progressIndicator.visibility = View.GONE
                        }
                    }
                }
            }
        }

        binding.btnToLogin.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun checkFieldsForErrors() {
        val usernameError = binding.edRegisterUsername.error != null
        val emailError = binding.edRegisterEmail.error != null
        val passwordError = binding.edRegisterPassword.error != null

        binding.btnRegister.isEnabled = !emailError && !passwordError && !usernameError
    }

    private fun playAnimation() {
        val logo = ObjectAnimator.ofFloat(binding.ivLogo, View.ALPHA, 1f).setDuration(100)
        val tvPageTitle =
            ObjectAnimator.ofFloat(binding.tvPageTitle, View.ALPHA, 1f).setDuration(100)
        val tvWelcome1 =
            ObjectAnimator.ofFloat(binding.tvWelcome1, View.ALPHA, 1f).setDuration(100)
        val usernameEditTextLayout =
            ObjectAnimator.ofFloat(binding.usernameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val btnRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(100)
        val tvRegister1 = ObjectAnimator.ofFloat(binding.tvRegister1, View.ALPHA, 1f).setDuration(100)
        val btnToLogin = ObjectAnimator.ofFloat(binding.btnToLogin, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                logo,
                tvPageTitle,
                tvWelcome1,
                usernameEditTextLayout,
                emailEditTextLayout,
                passwordEditTextLayout,
                btnRegister,
                tvRegister1,
                btnToLogin
            )
            startDelay = 100
        }.start()
    }
}