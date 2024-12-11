package com.dicoding.stunting.ui.main.journal

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dicoding.stunting.R
import com.dicoding.stunting.data.remote.nourish.response.ListJournalItem
import com.dicoding.stunting.databinding.ActivityAddJournalBinding
import com.dicoding.stunting.databinding.ActivityJournalDetailBinding
import com.dicoding.stunting.utils.formatDateTime

class JournalDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJournalDetailBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityJournalDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = resources.getColor(R.color.leaf_green, theme)
        supportActionBar?.hide()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val journalDate = intent.getStringExtra(EXTRA_JOURNAL_DATE)
        val journalDesc = intent.getStringExtra(EXTRA_JOURNAL_DESC)
        val journalPhoto = intent.getStringExtra(EXTRA_JOURNAL_PHOTO_URL)

        setupView(journalDate, journalDesc, journalPhoto)
        setupAction()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupView (date: String?, description: String?, photoUrl: String?) {
        Glide.with(this)
            .load(photoUrl)
            .error(R.drawable.image_placeholder)
            .into(binding.ivJournal)

        binding.journalDate.text = formatDateTime(date.toString())
        binding.journalDescription.text = description.toString()
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            finishAfterTransition()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finishAfterTransition()
        return true
    }

    companion object {
        const val EXTRA_JOURNAL_DATA = "JOURNAL_DATA"
        const val EXTRA_JOURNAL_DATE = "JOURNAL_DATE"
        const val EXTRA_JOURNAL_DESC = "JOURNAL_DESC"
        const val EXTRA_JOURNAL_PHOTO_URL = "JOURNAL_PHOTO_URL"
    }
}