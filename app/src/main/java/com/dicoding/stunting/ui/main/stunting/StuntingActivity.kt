package com.dicoding.stunting.ui.main.stunting

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.stunting.R
import com.dicoding.stunting.databinding.ActivityStuntingBinding
import com.dicoding.stunting.ui.helper.StuntingHelper

class StuntingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStuntingBinding

    private lateinit var stuntingHelper: StuntingHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityStuntingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        window.statusBarColor = resources.getColor(R.color.leaf_green, theme)
        supportActionBar?.hide()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupAction()
        checkFieldsForErrors()
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        val items = listOf(getString(R.string.male), getString(R.string.female))
        val adapter = ArrayAdapter(this, R.layout.list_item_gender, items)
        (binding.genderInput as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.edAgeInput.addTextChangedListener(inputListener())
        binding.edHeightInput.addTextChangedListener(inputListener())
        binding.genderInput.addTextChangedListener(inputListener())

        stuntingHelper = StuntingHelper(
            context = this,
            onResult = { result ->
                binding.tvResult.text = result
                binding.tvResult.visibility = View.VISIBLE
            },
            onError = {errorMessage ->
                Toast.makeText(this@StuntingActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        )

        binding.btnAnalyze.setOnClickListener {
            val age = binding.edAgeInput.text.toString().toIntOrNull()
            val height = binding.edHeightInput.text.toString().toIntOrNull()
            val genderValue = when (binding.genderInput.text.toString()) {
                getString(R.string.male) -> 0
                getString(R.string.female) -> 1
                else -> null
            }
            Log.d("Age:", age.toString())
            Log.d("height:", height.toString())
            Log.d("genderValue:", genderValue.toString())

            stuntingHelper.predict(age, height, genderValue)


        }
    }

    private fun inputListener(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkFieldsForErrors()
            }
            override fun afterTextChanged(s: Editable?) {}
        }
    }

    private fun checkFieldsForErrors() {
        val ageError = binding.edAgeInput.error != null || binding.edAgeInput.text.isNullOrBlank()
        val heightError = binding.edHeightInput.error != null || binding.edHeightInput.text.isNullOrBlank()
        val genderError = binding.genderInput.error != null || binding.genderInput.text.isNullOrBlank()

        val isEnabled = !ageError && !heightError && !genderError
        binding.btnAnalyze.isEnabled = isEnabled

        if (isEnabled) {
            binding.btnAnalyze.setBackgroundResource(R.drawable.rounded_corner)
        } else {
            binding.btnAnalyze.setBackgroundResource(R.drawable.rounded_disabled_button)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stuntingHelper.close()
    }
}