package com.dicoding.stunting.ui.main.history

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.stunting.R
import com.dicoding.stunting.data.local.entity.PredictionHistoryEntity
import com.dicoding.stunting.databinding.FragmentHistoryBinding
import com.dicoding.stunting.ui.ViewModelFactory
import com.dicoding.stunting.ui.adapter.PredictionHistoryAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.dicoding.stunting.data.remote.Result
import com.dicoding.stunting.ui.adapter.JournalAdapter
import com.dicoding.stunting.ui.authentication.AuthenticationViewModel
import com.dicoding.stunting.ui.main.profile.UserIdentityActivity

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val historyViewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private val authenticationViewModel: AuthenticationViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    private fun setupView() {
        val predictionAdapter = PredictionHistoryAdapter()
        authenticationViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                historyViewModel.getPredictionHistory(user.userId).observe(requireActivity()) { result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.tvHistoryNotFound.visibility = View.GONE
                            binding.overlayView.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            val prediction = result.data.map { predictionEntity ->
                                PredictionHistoryEntity(
                                    predictionId = predictionEntity.predictionId,
                                    age = predictionEntity.age,
                                    gender = predictionEntity.gender,
                                    height = predictionEntity.height,
                                    result = predictionEntity.result,
                                    description = predictionEntity.description,
                                    userId = predictionEntity.userId,
                                    createdAt = predictionEntity.createdAt
                                )
                            }
                            if (prediction.isEmpty()) {
                                binding.tvHistoryNotFound.visibility = View.VISIBLE
                            } else {
                                predictionAdapter.submitList(prediction)
                                binding.tvHistoryNotFound.visibility = View.GONE
                            }
                            binding.overlayView.visibility = View.GONE
                            binding.progressBar.visibility = View.GONE
                        }
                        is Result.Error -> {
                            Toast.makeText(requireActivity(), result.error, Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.GONE
                            binding.overlayView.visibility = View.GONE
                            binding.tvHistoryNotFound.visibility = View.VISIBLE
                        }
                    }
                }
                showRecyclerList(predictionAdapter)
            }
        }

    }

    private fun showRecyclerList(predictionAdapter: PredictionHistoryAdapter) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvHistory.layoutManager = GridLayoutManager(requireActivity(), 2)
        } else {
            binding.rvHistory.layoutManager = LinearLayoutManager(requireActivity())
        }
        binding.rvHistory.adapter = predictionAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}