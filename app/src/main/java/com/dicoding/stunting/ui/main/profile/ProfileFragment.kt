package com.dicoding.stunting.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
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
                binding.tvUsername.text = user.name
                binding.tvEmail.text = user.email
            } else {
                navigateToAuthentication()
            }
        }
    }

    private fun navigateToAuthentication() {
        startActivity(Intent(requireActivity(), AuthenticationActivity::class.java))
        requireActivity().finish()
    }

    private fun setupActions() {
        val profileItems = listOf(
            ProfileAction(getString(R.string.profile_item_account), R.drawable.ic_baseline_account_circle_24),
            ProfileAction(getString(R.string.profile_item_journal), R.drawable.baseline_journal_24),
            ProfileAction(getString(R.string.profile_item_language), R.drawable.ic_baseline_language_24),
            ProfileAction(getString(R.string.profile_item_reminder), R.drawable.ic_baseline_notifications_24, true)
        )

        val adapter = ProfileActionsAdapter(profileItems) { action ->
            handleProfileActionClick(action)
        }

        binding.profileActionsRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter

            val divider = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            divider.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider_line)!!)
            addItemDecoration(divider)
        }

        binding.btnLogout.setOnClickListener {
            authenticationViewModel.logout()
        }
    }

    private fun handleProfileActionClick(action: ProfileAction) {
        when (action.title) {
            getString(R.string.profile_item_account) -> navigateToUserIdentity()
            getString(R.string.profile_item_language) -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            getString(R.string.profile_item_reminder) -> showToast("Reminder clicked")
            getString(R.string.profile_item_journal) -> showToast("Journal clicked")
        }
    }

    private fun navigateToUserIdentity() {
        authenticationViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                val intent = Intent(requireContext(), UserIdentityActivity::class.java).apply {
                    putExtra(UserIdentityActivity.EXTRA_USERNAME, user.name)
                    putExtra(UserIdentityActivity.EXTRA_EMAIL, user.email)
                }
                startActivity(intent)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
