package com.dicoding.stunting.ui.main.stunting

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.stunting.R
import com.dicoding.stunting.data.remote.Result
import com.dicoding.stunting.databinding.ActivityFoodRecommendationBinding
import com.dicoding.stunting.databinding.ActivityIntroductionBinding
import com.dicoding.stunting.databinding.ActivityStuntingResultBinding
import com.dicoding.stunting.ui.ViewModelFactory
import com.dicoding.stunting.ui.adapter.FoodRecAdapter
import com.dicoding.stunting.ui.adapter.NewsAdapter
import com.dicoding.stunting.ui.main.stunting.StuntingResultActivity.Companion.EXTRA_PREDICTION_AGE

class FoodRecommendationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodRecommendationBinding

    private val stuntingViewModel: StuntingViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityFoodRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = resources.getColor(R.color.leaf_green, theme)
        supportActionBar?.hide()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val classification = intent.getIntExtra(EXTRA_CLASSIFICATION, 4)

        setupView(classification)
        setupAction()
    }

    private fun setupView (classification: Int) {
        val foodRecAdapter = FoodRecAdapter()
        stuntingViewModel.getFoodRecommendation(classification).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.tvFoodNotFound.visibility = View.GONE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvFoodNotFound.visibility = View.GONE
                        val foodRec = result.data.recommendations
                        foodRecAdapter.submitList(foodRec)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvFoodNotFound.visibility = View.VISIBLE
                        Toast.makeText(
                            this,
                            result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            showRecyclerList(foodRecAdapter)
        }
    }

    private fun showRecyclerList(foodRecAdapter: FoodRecAdapter) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvFoodRec.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvFoodRec.layoutManager = LinearLayoutManager(this)
        }
        binding.rvFoodRec.adapter = foodRecAdapter
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val EXTRA_CLASSIFICATION = "CLASSIFICATION"
    }
}