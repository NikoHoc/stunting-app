package com.dicoding.stunting.ui.main.profile

import android.os.Bundle
import android.text.Editable
import android.text.method.PasswordTransformationMethod
import android.text.method.HideReturnsTransformationMethod
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.dicoding.stunting.R
import com.dicoding.stunting.databinding.ActivityUserIdentityBinding

class UserIdentityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserIdentityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserIdentityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = resources.getColor(R.color.leaf_green, theme)
        supportActionBar?.hide()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val username = intent.getStringExtra(EXTRA_USERNAME) ?: getString(R.string.unknown_user)
        val email = intent.getStringExtra(EXTRA_EMAIL) ?: getString(R.string.unknown_email)
        val password = intent.getStringExtra(EXTRA_PASSWORD) ?: getString(R.string.unknown_password)

        binding.tvUsername.text = Editable.Factory.getInstance().newEditable(username)
        binding.tvEmail.text = Editable.Factory.getInstance().newEditable(email)
        binding.etPassword.setText(password)


        binding.btnTogglePassword.setOnClickListener {
            if (binding.etPassword.transformationMethod is HideReturnsTransformationMethod) {
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.btnTogglePassword.setImageResource(R.drawable.ic_password_hidden)
            } else {
                binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.btnTogglePassword.setImageResource(R.drawable.ic_baseline_password_24)
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_PASSWORD = "extra_password"
    }
}
