package com.dicoding.stunting.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.stunting.R
import com.dicoding.stunting.data.remote.nourish.response.ListJournalItem
import com.dicoding.stunting.databinding.ItemJournalLayoutBinding
import com.dicoding.stunting.ui.main.journal.JournalDetailActivity
import com.dicoding.stunting.utils.formatDate


class JournalAdapter: ListAdapter<ListJournalItem, JournalAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemJournalLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
    }

    class MyViewHolder(private val binding: ItemJournalLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(journal: ListJournalItem) {
            Glide.with(binding.ivJournal.context)
                .load(journal.photoUrl)
                .error(R.drawable.image_placeholder)
                .into(binding.ivJournal)

            binding.journalDate.text = formatDate(journal.createdAt.toString())

            binding.root.setOnClickListener {
                val context = it.context
                val intent = Intent(context, JournalDetailActivity::class.java)
                intent.putExtra(JournalDetailActivity.EXTRA_JOURNAL_DATA, journal)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context as Activity,
                        Pair(binding.ivJournal, "journal_image"),
                        Pair(binding.journalDate, "journal_date")
                    )

                context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListJournalItem>() {
            override fun areItemsTheSame(oldItem: ListJournalItem, newItem: ListJournalItem): Boolean {
                return oldItem == newItem
            }
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: ListJournalItem, newItem: ListJournalItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}