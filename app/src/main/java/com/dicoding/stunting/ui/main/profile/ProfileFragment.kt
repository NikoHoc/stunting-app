package com.dicoding.stunting.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dicoding.stunting.R
import com.dicoding.stunting.databinding.FragmentProfileBinding
import com.dicoding.stunting.ui.ViewModelFactory
import com.dicoding.stunting.ui.authentication.AuthenticationViewModel
import com.dicoding.stunting.ui.main.journal.AddJournalActivity
import com.dicoding.stunting.ui.main.journal.JournalHistoryActivity

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val authenticationViewModel: AuthenticationViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupAction()
    }

    private fun setupView () {
        authenticationViewModel.getSession().observe(viewLifecycleOwner) { user ->
            binding.tvUsername.text = resources.getString(R.string.username_profile, user.name)
            binding.tvEmail.text = resources.getString(R.string.email_profile, user.email)
        }
    }

    private fun setupAction() {
        binding.btnLogout.setOnClickListener{
            authenticationViewModel.logout()
        }

        binding.btnJournal.setOnClickListener {
            val intent = Intent(requireContext(), JournalHistoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}