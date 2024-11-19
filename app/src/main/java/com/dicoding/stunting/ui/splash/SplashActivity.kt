package com.dicoding.stunting.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.dicoding.stunting.R
import com.dicoding.stunting.databinding.ActivitySplashBinding
import com.dicoding.stunting.ui.ViewModelFactory
import com.dicoding.stunting.ui.authentication.AuthenticationActivity
import com.dicoding.stunting.ui.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    private val splashViewModel  by viewModels<SplashViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        lifecycleScope.launch {
            splashViewModel.getSession().observe(this@SplashActivity) { userModel ->
                if (userModel.isLogin) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                    return@observe
                }
            }
            delay(3000L)
            if (!isFinishing) goToAuthenticationActivity()
        }
    }

    private fun goToAuthenticationActivity() {
        Intent(this, AuthenticationActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
}