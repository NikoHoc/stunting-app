package com.dicoding.stunting.ui.main.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.stunting.databinding.ItemProfileActionBinding

class ProfileActionsAdapter(
    private val actions: List<ProfileAction>,
    private val onItemClick: (ProfileAction) -> Unit
) : RecyclerView.Adapter<ProfileActionsAdapter.ProfileActionViewHolder>() {

    inner class ProfileActionViewHolder(private val binding: ItemProfileActionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(action: ProfileAction) {
            binding.tvActionTitle.text = action.title
            binding.ivActionIcon.setImageResource(action.iconRes)
            binding.root.setOnClickListener {
                onItemClick(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileActionViewHolder {
        val binding = ItemProfileActionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProfileActionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileActionViewHolder, position: Int) {
        holder.bind(actions[position])
    }

    override fun getItemCount(): Int = actions.size
}

data class ProfileAction(val title: String, val iconRes: Int)
