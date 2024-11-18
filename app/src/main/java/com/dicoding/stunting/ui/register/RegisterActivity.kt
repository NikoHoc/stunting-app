package com.dicoding.stunting.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.stunting.R
import com.dicoding.stunting.databinding.ActivityRegisterBinding
import com.dicoding.stunting.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

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


        binding.btnToLogin.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
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