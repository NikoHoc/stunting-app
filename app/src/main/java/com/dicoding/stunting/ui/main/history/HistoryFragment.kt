package com.dicoding.stunting.ui.main.history

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import com.dicoding.stunting.utils.formatDateChart
import com.dicoding.stunting.utils.formatDateTime
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
                            binding.chartLayout.visibility = View.VISIBLE
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
                                binding.chartLayout.visibility = View.VISIBLE
                                setupBarChart(prediction)
                            }
                            binding.overlayView.visibility = View.GONE
                            binding.progressBar.visibility = View.GONE
                        }
                        is Result.Error -> {
                            Toast.makeText(requireActivity(), result.error, Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.GONE
                            binding.overlayView.visibility = View.GONE
                            binding.tvHistoryNotFound.visibility = View.VISIBLE
                            binding.chartLayout.visibility = View.VISIBLE
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupBarChart(predictionList: List<PredictionHistoryEntity>) {
        val barChart: BarChart = binding.barChart

        val heightEntries = ArrayList<BarEntry>()
        val ageEntries = ArrayList<BarEntry>()
        val dateLabels = ArrayList<String>()

        predictionList.forEachIndexed { index, prediction ->
            val date = formatDateChart(prediction.createdAt.toString())
            val height = prediction.height ?: 0f
            val age = prediction.age?.toFloat() ?: 0f

            heightEntries.add(BarEntry(index.toFloat(), height)) // x = index, y = height
            ageEntries.add(BarEntry(index.toFloat(), age)) // x = index, y = age
            dateLabels.add(date) // x-axis labels
        }

        val heightDataSet = BarDataSet(heightEntries, "Height (cm)").apply {
            color = resources.getColor(R.color.teal_blue, null)
            valueTextColor = resources.getColor(R.color.teal_blue, null)
            valueTextSize = 12f
        }

        val ageDataSet = BarDataSet(ageEntries, "Age (month)").apply {
            color = resources.getColor(R.color.deep_blue, null)
            valueTextColor = resources.getColor(R.color.deep_blue, null)
            valueTextSize = 12f
        }

        val barData = BarData(heightDataSet, ageDataSet).apply {
            barWidth = 0.3f
        }

        val groupSpace = 0.35f
        val barSpace = 0.05f
        val start = 0f
        barData.groupBars(start, groupSpace, barSpace)

        barChart.apply {
            data = barData
            description.isEnabled = false
            setFitBars(true)

            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(dateLabels)
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                textSize = 10f
                granularity = 1f
                labelCount = dateLabels.size
                axisMinimum = start
                axisMaximum = predictionList.size.toFloat()
//                labelRotationAngle = 15f
            }

            axisLeft.apply {
                setDrawGridLines(false)
                axisMinimum = 0f
            }

            axisRight.isEnabled = false

            legend.apply {
                isEnabled = true
                textSize = 12f
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                orientation = Legend.LegendOrientation.HORIZONTAL
                setDrawInside(false)
            }

            moveViewToX(0f) // Start viewing from the first bar
            invalidate() // Refresh the chart
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}