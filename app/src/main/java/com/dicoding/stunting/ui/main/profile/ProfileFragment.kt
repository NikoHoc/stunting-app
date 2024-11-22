package com.dicoding.stunting.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.stunting.R
import com.dicoding.stunting.databinding.FragmentProfileBinding
import com.dicoding.stunting.ui.ViewModelFactory
import com.dicoding.stunting.ui.authentication.AuthenticationActivity
import com.dicoding.stunting.ui.authentication.AuthenticationViewModel
import com.dicoding.stunting.ui.main.profile.adapter.ProfileActionsAdapter
import com.dicoding.stunting.ui.main.profile.adapter.ProfileAction

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val authenticationViewModel: AuthenticationViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupActions()
    }

    private fun setupView() {
        authenticationViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                binding.tvUsername.text = resources.getString(R.string.username_profile, user.name)
                binding.tvEmail.text = resources.getString(R.string.email_profile, user.email)
            } else {
                startActivity(Intent(requireActivity(), AuthenticationActivity::class.java))
                requireActivity().finish()
            }
        }
    }

    private fun setupActions() {
        val profileItems = listOf(
            ProfileAction(getString(R.string.profile_item_nutrition_tips), R.drawable.ic_baseline_nutrition_24),
            ProfileAction(getString(R.string.profile_item_vaccination_schedule), R.drawable.ic_baseline_vaccines_24),
            ProfileAction(getString(R.string.profile_item_health_articles), R.drawable.ic_baseline_article_24),
            ProfileAction(getString(R.string.profile_item_child_growth_tracker), R.drawable.ic_baseline_tracker_24),
            ProfileAction(getString(R.string.profile_item_reminder), R.drawable.ic_baseline_reminder_24)
        )

        val adapter = ProfileActionsAdapter(profileItems) { action ->
            when (action.title) {
                getString(R.string.profile_item_nutrition_tips) -> {
                }
                getString(R.string.profile_item_vaccination_schedule) -> {
                }
                getString(R.string.profile_item_health_articles) -> {
                }
                getString(R.string.profile_item_child_growth_tracker) -> {
                }
                getString(R.string.profile_item_reminder) -> {
                }
            }
        }

        binding.profileActionsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.profileActionsRecycler.adapter = adapter

        binding.btnLogout.setOnClickListener {
            authenticationViewModel.logout()
        }

        binding.settingImageView.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
