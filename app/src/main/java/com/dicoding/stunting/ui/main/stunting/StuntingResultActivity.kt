package com.dicoding.stunting.ui.main.stunting

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.dicoding.stunting.R
import com.dicoding.stunting.databinding.ActivityStuntingResultBinding
import com.dicoding.stunting.ui.ViewModelFactory
import com.dicoding.stunting.ui.main.history.HistoryViewModel
import com.dicoding.stunting.data.remote.Result

class StuntingResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStuntingResultBinding

    private val historyViewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

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
        val age = intent.getIntExtra(EXTRA_PREDICTION_AGE, 0)
        val height = intent.getFloatExtra(EXTRA_PREDICTION_HEIGHT, -1F)
        val gender = intent.getStringExtra(EXTRA_PREDICTION_GENDER).toString()
        val result = intent.getStringExtra(EXTRA_PREDICTION_RESULT).toString()
        val description = intent.getStringExtra(EXTRA_PREDICTION_DESC).toString()

        setupView(age, height, gender, result, description)
        setupAction(age, height, gender, result, description)
    }

    private fun setupView (age: Int, height: Float, gender: String, result: String, description: String) {
        binding.apply {
            tvUserAge.text = getString(R.string.user_age, age)
            tvUserHeight.text = getString(R.string.user_height, height)
            tvUserGender.text = getString(R.string.user_gender, gender)

            tvResult.text = getString(R.string.result, result)
            tvDescription.text = description
        }
    }

    private fun setupAction (age: Int, height: Float, gender: String, result: String, description: String) {
        binding.btnBack.setOnClickListener { finish() }

        binding.btnSave.setOnClickListener {
            val descriptions = "tes"
            historyViewModel.uploadPredict(age, gender, height, result, descriptions).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        val response = result.data
                        AlertDialog.Builder(this).apply {
                            setTitle(R.string.up_predict_success_title)
                            setMessage(response.message)
                            setPositiveButton(R.string.success_up_predict_reply) { dialog, _ ->
                                dialog.dismiss()
                            }
                            create()
                            show()
                        }
                        binding.progressBar.visibility = View.GONE
                        binding.btnSave.visibility = View.GONE
                    }
                    is Result.Error -> {
                        AlertDialog.Builder(this).apply {
                            setTitle(R.string.up_predict_failed_title)
                            setMessage(result.error)
                            setPositiveButton(R.string.error_alert_reply) { dialog, _ ->
                                dialog.dismiss()
                            }
                            create()
                            show()
                        }
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_PREDICTION_AGE  = "PREDICTION_AGE"
        const val EXTRA_PREDICTION_HEIGHT  = "PREDICTION_HEIGHT"
        const val EXTRA_PREDICTION_GENDER = "PREDICTION_GENDER"
        const val EXTRA_PREDICTION_RESULT  = "PREDICTION_RESULT"
        const val EXTRA_PREDICTION_DESC = "PREDICTION_DESC"
    }
}