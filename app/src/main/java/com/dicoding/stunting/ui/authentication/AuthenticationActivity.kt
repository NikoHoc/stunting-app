package com.dicoding.stunting.ui.authentication

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.dicoding.stunting.R
import com.dicoding.stunting.databinding.ActivityAuthenticationBinding
import com.dicoding.stunting.databinding.ActivityMainBinding
import com.dicoding.stunting.ui.authentication.login.LoginFragment
import com.dicoding.stunting.ui.main.home.HomeFragment

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        window.statusBarColor = resources.getColor(R.color.mint_green, theme)
        supportActionBar?.hide()

        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.authentication_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fragmentManager = supportFragmentManager
        val loginFragment = LoginFragment()
        val fragment = fragmentManager.findFragmentByTag(LoginFragment::class.java.simpleName)

        if (fragment !is LoginFragment) {
            fragmentManager.commit {
                add(R.id.authentication_activity, loginFragment, LoginFragment::class.java.simpleName)
            }
        }

    }
}