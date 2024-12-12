package com.dicoding.stunting.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.getString
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.stunting.R
import com.dicoding.stunting.data.local.entity.JournalHistoryEntity
import com.dicoding.stunting.data.local.entity.PredictionHistoryEntity
import com.dicoding.stunting.data.remote.nourish.response.ListJournalItem
import com.dicoding.stunting.databinding.ItemCardLayoutBinding
import com.dicoding.stunting.databinding.ItemPredictionLayoutBinding
import com.dicoding.stunting.ui.main.journal.JournalDetailActivity
import com.dicoding.stunting.ui.main.stunting.StuntingResultActivity
import com.dicoding.stunting.utils.formatDateTime


class PredictionHistoryAdapter: ListAdapter<PredictionHistoryEntity, PredictionHistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPredictionLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val prediction = getItem(position)
        holder.bind(prediction)
    }

    class MyViewHolder(private val binding: ItemPredictionLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(prediction: PredictionHistoryEntity) {

            binding.tvDatePrediction.text = formatDateTime(prediction.createdAt.toString())
            binding.tvResult.text = binding.root.context.getString(R.string.result, prediction.result)

            binding.btnDetail.setOnClickListener {
                val context = it.context
                val intent = Intent(context, StuntingResultActivity::class.java)

                intent.putExtra(StuntingResultActivity.EXTRA_PREDICTION_AGE, prediction.age)
                intent.putExtra(StuntingResultActivity.EXTRA_PREDICTION_GENDER, prediction.gender)
                intent.putExtra(StuntingResultActivity.EXTRA_PREDICTION_HEIGHT, prediction.height)
                intent.putExtra(StuntingResultActivity.EXTRA_PREDICTION_RESULT, prediction.result)
                intent.putExtra(StuntingResultActivity.EXTRA_PREDICTION_DESC, prediction.description)

                Log.d("res", prediction.result.toString())
                context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PredictionHistoryEntity>() {
            override fun areItemsTheSame(oldItem: PredictionHistoryEntity, newItem: PredictionHistoryEntity): Boolean {
                return oldItem == newItem
            }
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: PredictionHistoryEntity, newItem: PredictionHistoryEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}