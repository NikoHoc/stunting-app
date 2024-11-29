package com.dicoding.stunting.ui.main.profile

import android.os.Bundle
import android.text.Editable
import android.text.method.PasswordTransformationMethod
import android.text.method.HideReturnsTransformationMethod
import androidx.appcompat.app.AppCompatActivity
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

        val username = intent.getStringExtra(EXTRA_USERNAME) ?: "Unknown User"
        val email = intent.getStringExtra(EXTRA_EMAIL) ?: "Unknown Email"

        binding.tvUsername.text = Editable.Factory.getInstance().newEditable(username)
        binding.tvEmail.text = Editable.Factory.getInstance().newEditable(email)

        val password = intent.getStringExtra(EXTRA_PASSWORD) ?: "Password not available"
        binding.etPassword.setText(password)

        binding.btnTogglePassword.setOnClickListener {
            if (binding.etPassword.transformationMethod is HideReturnsTransformationMethod) {
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.btnTogglePassword.setImageResource(R.drawable.ic_baseline_password_24)
            } else {
                binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.btnTogglePassword.setImageResource(R.drawable.ic_baseline_off_password_24)
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
