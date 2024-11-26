package com.dicoding.stunting.ui.main.journal

import android.content.Intent
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
import com.dicoding.stunting.databinding.ActivityJournalHistoryBinding
import com.dicoding.stunting.databinding.ActivityMainBinding
import com.dicoding.stunting.data.remote.Result
import com.dicoding.stunting.ui.ViewModelFactory
import com.dicoding.stunting.ui.authentication.AuthenticationActivity
import com.dicoding.stunting.ui.authentication.AuthenticationViewModel
import com.dicoding.stunting.ui.main.journal.adapater.JournalAdapter

class JournalHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJournalHistoryBinding

    private val journalViewModel  by viewModels<JournalViewModel> {
        ViewModelFactory.getInstance(application)
    }

    private val authenticationViewModel  by viewModels<AuthenticationViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityJournalHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = resources.getColor(R.color.leaf_green, theme)
        supportActionBar?.hide()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupAction()
        setupView()
    }

    private fun setupAction() {
        binding.addJournal.setOnClickListener {
            val intent = Intent(this, AddJournalActivity::class.java)
            startActivity(intent)
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupView() {
        val journalAdapter = JournalAdapter()
        authenticationViewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, AuthenticationActivity::class.java))
                finish()
            } else {
                journalViewModel.getJournal().observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            val story = result.data.listStory
                            journalAdapter.submitList(story)

                            Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.GONE
                            binding.tvJournalNotFound.visibility = View.GONE
                        }
                        is Result.Error -> {
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.GONE
                            binding.tvJournalNotFound.visibility = View.VISIBLE
                        }
                    }
                }
                showRecyclerList(journalAdapter)
            }
        }
    }

    private fun showRecyclerList(journalAdapter: JournalAdapter) {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvJournal.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvJournal.layoutManager = LinearLayoutManager(this)
        }
        binding.rvJournal.adapter = journalAdapter
    }
}