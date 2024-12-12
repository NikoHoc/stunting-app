package com.dicoding.stunting.ui.introduction.material

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.dicoding.stunting.R
import com.dicoding.stunting.databinding.FragmentDefinitionBinding
import com.dicoding.stunting.databinding.FragmentFactsBinding

class FactsFragment : Fragment() {
    private var _binding: FragmentFactsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFactsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            parentFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.introduction_activity, IssueFragment(),  IssueFragment::class.java.simpleName)
            }
        }
    }

}