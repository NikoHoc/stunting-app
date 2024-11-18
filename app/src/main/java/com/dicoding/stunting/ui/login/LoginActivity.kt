package com.dicoding.stunting.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.stunting.R
import com.dicoding.stunting.data.pref.UserModel
import com.dicoding.stunting.data.remote.Result
import com.dicoding.stunting.databinding.ActivityLoginBinding
import com.dicoding.stunting.ui.ViewModelFactory
import com.dicoding.stunting.ui.main.MainActivity
import com.dicoding.stunting.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel  by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
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

    private fun setupAction () {
        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            loginViewModel.login(email, password).observe(this) {result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressIndicator.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            val loginResponse = result.data
                            val userModel = UserModel(
                                userId = loginResponse.loginResult?.userId.toString(),
                                name = loginResponse.loginResult?.name.toString(),
                                email = email,
                                token = loginResponse.loginResult?.token.toString(),
                                isLogin = true
                            )
                            loginViewModel.saveSession(userModel)

                            AlertDialog.Builder(this).apply {
                                setTitle(R.string.login_success_alert_title)
                                setMessage(loginResponse.message)
                                setPositiveButton(R.string.success_alert_reply) { _, _ ->
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                            binding.progressIndicator.visibility = View.GONE
                        }
                        is Result.Error -> {
                            AlertDialog.Builder(this).apply {
                                setTitle(R.string.login_error_alert_title)
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

        binding.btnToSignup.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this as Activity).toBundle())
        }
    }

    private fun playAnimation() {
        val logo = ObjectAnimator.ofFloat(binding.ivLogo, View.ALPHA, 1f).setDuration(100)
        val tvNourish =
            ObjectAnimator.ofFloat(binding.tvNourish, View.ALPHA, 1f).setDuration(100)
        val tvWelcome1 =
            ObjectAnimator.ofFloat(binding.tvWelcome1, View.ALPHA, 1f).setDuration(100)
        val tvWelcome2 =
            ObjectAnimator.ofFloat(binding.tvWelcome2, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(100)
        val tvRegister1 = ObjectAnimator.ofFloat(binding.tvRegister1, View.ALPHA, 1f).setDuration(100)
        val btnToRegister = ObjectAnimator.ofFloat(binding.btnToSignup, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                logo,
                tvNourish,
                tvWelcome1,
                tvWelcome2,
                emailEditTextLayout,
                passwordEditTextLayout,
                btnLogin,
                tvRegister1,
                btnToRegister
            )
            startDelay = 100
        }.start()
    }

}