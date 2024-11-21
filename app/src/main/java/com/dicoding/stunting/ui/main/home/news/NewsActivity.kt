package com.dicoding.stunting.ui.main.home.news

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.stunting.R
import com.dicoding.stunting.data.remote.Result
import com.dicoding.stunting.databinding.ActivityMainBinding
import com.dicoding.stunting.databinding.ActivityNewsBinding
import com.dicoding.stunting.ui.ViewModelFactory
import com.dicoding.stunting.ui.main.home.NewsViewModel
import com.dicoding.stunting.ui.main.home.adapter.NewsAdapter

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding

    private val newsViewModel by viewModels<NewsViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityNewsBinding.inflate(layoutInflater)
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

    private fun setupView() {
        val newsAdapter = NewsAdapter()
        newsViewModel.getNews().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.tvNewsNotFound.visibility = View.GONE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvNewsNotFound.visibility = View.GONE
                        val news = result.data
                        newsAdapter.submitList(news)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvNewsNotFound.visibility = View.VISIBLE
                        Toast.makeText(
                            this,
                            result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            showRecyclerList(newsAdapter)
        }
    }

    private fun showRecyclerList(newsAdapter: NewsAdapter) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvNewsStunting.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvNewsStunting.layoutManager = LinearLayoutManager(this)
        }
        binding.rvNewsStunting.adapter = newsAdapter
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener{
            finish()
        }
    }
}