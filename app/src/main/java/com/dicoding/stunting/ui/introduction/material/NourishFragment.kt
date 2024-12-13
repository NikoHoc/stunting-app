package com.dicoding.stunting.ui.introduction.material

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.dicoding.stunting.R
import com.dicoding.stunting.databinding.FragmentNourishBinding
import com.dicoding.stunting.ui.main.home.HomeFragment
import com.dicoding.stunting.ui.main.MainActivity

class NourishFragment : Fragment() {

    private var _binding: FragmentNourishBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNourishBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        playAnimation()
    }

    private fun playAnimation() {
        val tvTitle = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(200)
        val tvDescription =
            ObjectAnimator.ofFloat(binding.tvDescription, View.ALPHA, 1f).setDuration(200)
        val tvDesc2 =
            ObjectAnimator.ofFloat(binding.tvDesc2, View.ALPHA, 1f).setDuration(200)
        val tvDesc3 =
            ObjectAnimator.ofFloat(binding.tvDesc3, View.ALPHA, 1f).setDuration(200)
        val tvDesc34 =
            ObjectAnimator.ofFloat(binding.tvDesc34, View.ALPHA, 1f).setDuration(200)
        val btnNext =
            ObjectAnimator.ofFloat(binding.btnNext, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(
                tvTitle,
                tvDescription,
                tvDesc2,
                tvDesc3,
                tvDesc34,
                btnNext
            )
            startDelay = 200
        }.start()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
