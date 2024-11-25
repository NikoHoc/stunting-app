package com.dicoding.stunting.ui.main.home.news.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.stunting.R
import com.dicoding.stunting.data.local.entity.NewsEntity
import com.dicoding.stunting.databinding.ItemNewsLayoutBinding
import com.dicoding.stunting.ui.main.home.news.detail.NewsDetailActivity

class NewsAdapter: ListAdapter<NewsEntity, NewsAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemNewsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)
    }

    class MyViewHolder(private val binding: ItemNewsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: NewsEntity) {
            Glide.with(binding.ivNews.context)
                .load(news.urlToImage)
                .error(R.drawable.image_placeholder)
                .into(binding.ivNews)
            binding.newsTitle.text = news.title
            binding.root.setOnClickListener {
                val context = it.context
//                val intent = Intent(Intent.ACTION_VIEW).apply {
//                    data = Uri.parse(news.url)
//                }
                val intent = Intent(context, NewsDetailActivity::class.java).apply {
                    putExtra("URL", news.url)
                }
                context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsEntity>() {
            override fun areItemsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean {
                return oldItem == newItem
            }
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}