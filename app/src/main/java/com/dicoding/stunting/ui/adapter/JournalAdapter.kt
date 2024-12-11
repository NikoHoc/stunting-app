package com.dicoding.stunting.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.stunting.R
import com.dicoding.stunting.data.local.entity.JournalHistoryEntity
import com.dicoding.stunting.data.remote.nourish.response.ListJournalItem
import com.dicoding.stunting.databinding.ItemCardLayoutBinding
import com.dicoding.stunting.ui.main.journal.JournalDetailActivity
import com.dicoding.stunting.utils.formatDateTime


class JournalAdapter: ListAdapter<JournalHistoryEntity, JournalAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val journal = getItem(position)
        holder.bind(journal)
    }

    class MyViewHolder(private val binding: ItemCardLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(journal: JournalHistoryEntity) {
            Glide.with(binding.cardImage.context)
                .load(journal.photoUrl)
                .error(R.drawable.image_placeholder)
                .into(binding.cardImage)

            binding.cardTitle.text = formatDateTime(journal.journalDate.toString())

            binding.root.setOnClickListener {
                val context = it.context
                val intent = Intent(context, JournalDetailActivity::class.java)
                intent.putExtra(JournalDetailActivity.EXTRA_JOURNAL_DATE, journal.journalDate)
                intent.putExtra(JournalDetailActivity.EXTRA_JOURNAL_DESC, journal.description)
                intent.putExtra(JournalDetailActivity.EXTRA_JOURNAL_PHOTO_URL, journal.photoUrl)

                //intent.putExtra(JournalDetailActivity.EXTRA_JOURNAL_DATA, journal)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context as Activity,
                        Pair(binding.cardImage, "journal_image"),
                        Pair(binding.cardTitle, "journal_date")
                    )

                context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<JournalHistoryEntity>() {
            override fun areItemsTheSame(oldItem: JournalHistoryEntity, newItem: JournalHistoryEntity): Boolean {
                return oldItem == newItem
            }
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: JournalHistoryEntity, newItem: JournalHistoryEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}