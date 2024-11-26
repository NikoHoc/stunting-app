package com.dicoding.stunting.ui.main.home

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.stunting.R
import com.dicoding.stunting.data.remote.Result
import com.dicoding.stunting.databinding.FragmentHomeBinding
import com.dicoding.stunting.ui.ViewModelFactory
import com.dicoding.stunting.ui.authentication.AuthenticationViewModel
import com.dicoding.stunting.ui.authentication.register.RegisterFragment
import com.dicoding.stunting.ui.main.journal.AddJournalActivity
import com.dicoding.stunting.ui.main.MainActivity
import com.dicoding.stunting.ui.main.history.HistoryFragment
import com.dicoding.stunting.ui.main.news.adapter.NewsAdapter
import com.dicoding.stunting.ui.main.news.NewsFragment
import com.dicoding.stunting.ui.main.news.NewsViewModel
import com.dicoding.stunting.ui.main.profile.ProfileFragment

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val authenticationViewModel: AuthenticationViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private val newsViewModel: NewsViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupAction()
    }

    private fun setupView() {
        authenticationViewModel.getSession().observe(viewLifecycleOwner) { user ->
            binding.tvGreet.text = resources.getString(R.string.greet_user, user.name)
        }

        val newsAdapter = NewsAdapter()
        newsViewModel.getNews().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.tvNewsNotFound.visibility = View.GONE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvNewsNotFound.visibility = View.GONE
                        val news = result.data.take(5)
                        newsAdapter.submitList(news)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvNewsNotFound.visibility = View.VISIBLE
                        Toast.makeText(
                            context,
                            result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            showRecyclerList(newsAdapter)
        }

    }

    private fun showRecyclerList(newsAdapter: NewsAdapter) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvNewsStunting.layoutManager = GridLayoutManager(requireActivity(), 2)
        } else {
            binding.rvNewsStunting.layoutManager = LinearLayoutManager(requireActivity())
        }
        binding.rvNewsStunting.adapter = newsAdapter
    }

    private fun setupAction() {
        binding.btnMoreNews.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_navigation_home_to_newsFragment)
        }

        val navController = findNavController()

        binding.btnAnalyze1.setOnClickListener {

        }

        binding.btnAnalyze2.setOnClickListener {

        }

        binding.btnHistory.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_historyFragment)
        }

        binding.btnJournal.setOnClickListener {
            val intent = Intent(requireContext(), AddJournalActivity::class.java)
            startActivity(intent)
        }

        binding.btnProfile.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_profileFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}