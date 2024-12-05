package com.dicoding.stunting.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.stunting.R
import com.dicoding.stunting.data.local.pref.NotificationPreference
import com.dicoding.stunting.databinding.ItemProfileActionBinding
import com.dicoding.stunting.ui.helper.ProfileAction
import com.dicoding.stunting.ui.main.MainActivity

class ProfileActionsAdapter(
    private val actions: List<ProfileAction>,
    private val onItemClick: (ProfileAction) -> Unit
) : RecyclerView.Adapter<ProfileActionsAdapter.ProfileActionViewHolder>() {

    inner class ProfileActionViewHolder(private val binding: ItemProfileActionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(action: ProfileAction) {
            binding.tvActionTitle.text = action.title
            binding.ivActionIcon.setImageResource(action.iconRes)

            if (action.title == binding.root.context.getString(R.string.profile_item_reminder)) {
                val mainActivity = binding.root.context as MainActivity
                val pref = NotificationPreference(binding.root.context)

                binding.switchToggle.visibility = View.VISIBLE
                binding.ivArrow.visibility = View.GONE
                binding.switchToggle.isChecked = pref.isNotificationEnabled()

                binding.switchToggle.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        mainActivity.requestNotificationPermission()
                    } else {
                        pref.setNotificationEnabled(false)
                        mainActivity.setupDailyNotification(false)
                    }
                }

                // Pastikan garis divider tidak terlihat untuk "Reminder"
                binding.divider.visibility = View.GONE
            } else {
                binding.switchToggle.visibility = View.GONE
                binding.ivArrow.visibility = View.VISIBLE

                // Hanya sembunyikan garis divider untuk item terakhir
                if (adapterPosition == actions.size - 1) {
                    binding.divider.visibility = View.GONE
                } else {
                    binding.divider.visibility = View.VISIBLE
                }
            }

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
