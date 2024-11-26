package com.dicoding.stunting.ui.main.journal

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dicoding.stunting.R
import com.dicoding.stunting.data.remote.nourish.response.ListJournalItem
import com.dicoding.stunting.databinding.ActivityAddJournalBinding
import com.dicoding.stunting.databinding.ActivityJournalDetailBinding
import com.dicoding.stunting.ui.utils.formatDate

class JournalDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJournalDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityJournalDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = resources.getColor(R.color.leaf_green, theme)
        supportActionBar?.hide()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val journal = intent.getParcelableExtra<ListJournalItem>(EXTRA_JOURNAL_DATA)
        journal?.let {
            setData(it)
        }

        setupAction()
    }

    private fun setData(journal: ListJournalItem) {
        Glide.with(this)
            .load(journal.photoUrl)
            .error(R.drawable.image_placeholder)
            .into(binding.ivJournal)

        binding.journalDate.text = formatDate(journal.createdAt.toString())
        binding.journalDescription.text = journal.description
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
    }
}