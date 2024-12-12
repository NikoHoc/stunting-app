package com.dicoding.stunting.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.stunting.R
import com.dicoding.stunting.data.local.entity.NewsEntity
import com.dicoding.stunting.data.remote.nourish.response.FoodRecResponse
import com.dicoding.stunting.data.remote.nourish.response.RecommendationsItem
import com.dicoding.stunting.databinding.ItemCardLayoutBinding
import com.dicoding.stunting.databinding.ItemFoodRecLayoutBinding
import com.dicoding.stunting.ui.main.news.NewsDetailActivity

class FoodRecAdapter: ListAdapter<RecommendationsItem, FoodRecAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemFoodRecLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val foodRec = getItem(position)
        holder.bind(foodRec)
    }

    class MyViewHolder(private val binding: ItemFoodRecLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(foodRec: RecommendationsItem) {
            binding.apply {
                tvCategory.text = binding.root.context.getString(R.string.category_label, foodRec.category ?: "N/A")
                tvFood.text = binding.root.context.getString(R.string.food_label, foodRec.food ?: "N/A")
                tvServingSize.text = binding.root.context.getString(R.string.serving_size_label, foodRec.servingSize ?: "N/A")
                tvCalories.text = binding.root.context.getString(R.string.calories_label, foodRec.calories ?: "N/A")
                tvProtein.text = binding.root.context.getString(R.string.protein_label, foodRec.protein ?: "N/A")
                tvFats.text = binding.root.context.getString(R.string.fats_label, foodRec.fats?.toString() ?: "N/A")
                tvVitaminA.text = binding.root.context.getString(R.string.vitamin_a_label, foodRec.vitaminA ?: "N/A")
                tvVitaminC.text = binding.root.context.getString(R.string.vitamin_c_label, foodRec.vitaminC ?: "N/A")
                tvVitaminD.text = binding.root.context.getString(R.string.vitamin_d_label, foodRec.vitaminD ?: "N/A")
                tvVitaminE.text = binding.root.context.getString(R.string.vitamin_e_label, foodRec.vitaminE ?: "N/A")
                tvVitaminB1.text = binding.root.context.getString(R.string.vitamin_b1_label, foodRec.vitaminB1 ?: "N/A")
                tvVitaminB2.text = binding.root.context.getString(R.string.vitamin_b2_label, foodRec.vitaminB2 ?: "N/A")
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecommendationsItem>() {
            override fun areItemsTheSame(oldItem: RecommendationsItem, newItem: RecommendationsItem): Boolean {
                return oldItem == newItem
            }
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: RecommendationsItem, newItem: RecommendationsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}