package com.dicoding.stunting.ui.main.stunting

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.stunting.R
import com.dicoding.stunting.databinding.ActivityStuntingResultBinding

class StuntingResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStuntingResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityStuntingResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = resources.getColor(R.color.leaf_green, theme)
        supportActionBar?.hide()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupView()
        setupAction()
    }

    private fun setupView () {
        val age = intent.getIntExtra("AGE", 0)
        val height = intent.getFloatExtra("HEIGHT", -1F)
        val gender = when (intent.getIntExtra("GENDER", -1)) {
            0 -> getString(R.string.user_gender, getString(R.string.male))
            1 -> getString(R.string.user_gender, getString(R.string.female))
            else -> getString(R.string.user_gender, getString(R.string.unknown_gender))
        }

        val result = intent.getStringExtra("RESULT")
        val description = intent.getStringExtra("DESCRIPTION")


        binding.apply {
            tvUserAge.text = getString(R.string.user_age, age)
            tvUserHeight.text = getString(R.string.user_height, height)
            tvUserGender.text = gender

            tvResult.text = getString(R.string.result, result)
            tvDescription.text = description
        }

    }

    private fun setupAction () {
        binding.btnBack.setOnClickListener { finish() }
    }
}