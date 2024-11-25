package com.dicoding.stunting.ui.main.home.news

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.stunting.R
import com.dicoding.stunting.data.remote.Result
import com.dicoding.stunting.databinding.FragmentHomeBinding
import com.dicoding.stunting.databinding.FragmentNewsBinding
import com.dicoding.stunting.ui.ViewModelFactory
import com.dicoding.stunting.ui.main.home.adapter.NewsAdapter

class NewsFragment : Fragment() {
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private val newsViewModel: NewsViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupAction()
    }

    private fun setupView() {
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
                        val news = result.data
                        newsAdapter.submitList(news)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvNewsNotFound.visibility = View.VISIBLE
                        Toast.makeText(
                            requireContext(),
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
            binding.rvNewsStunting.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            binding.rvNewsStunting.layoutManager = LinearLayoutManager(requireContext())
        }
        binding.rvNewsStunting.adapter = newsAdapter
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}