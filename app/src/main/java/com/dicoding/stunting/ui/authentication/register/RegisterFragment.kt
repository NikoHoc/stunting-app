package com.dicoding.stunting.ui.authentication.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.dicoding.stunting.R
import com.dicoding.stunting.data.remote.Result
import com.dicoding.stunting.databinding.FragmentRegisterBinding
import com.dicoding.stunting.ui.ViewModelFactory

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel: RegisterViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playAnimation()
        checkFieldsForErrors()
        setupAction()
    }

    private fun setupAction() {
        binding.edRegisterUsername.addTextChangedListener(inputListener())
        binding.edRegisterEmail.addTextChangedListener(inputListener())
        binding.edRegisterPassword.addTextChangedListener(inputListener())

        binding.btnRegister.setOnClickListener {
            val username = binding.edRegisterUsername.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            registerViewModel.registerUser(username, email, password).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressIndicator.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        val registerResponse = result.data
                        AlertDialog.Builder(requireContext()).apply {
                            setTitle(R.string.signup_success_alert_title)
                            setMessage(registerResponse.message)
                            setPositiveButton(R.string.success_alert_reply) { _, _ ->
                                parentFragmentManager.popBackStack()
                            }
                            create()
                            show()
                        }
                        binding.progressIndicator.visibility = View.GONE
                    }
                    is Result.Error -> {
                        AlertDialog.Builder(requireContext()).apply {
                            setTitle(R.string.signup_error_alert_title)
                            setMessage(result.error)
                            setPositiveButton(R.string.error_alert_reply) { dialog, _ ->
                                dialog.dismiss()
                            }
                            create()
                            show()
                        }
                        binding.progressIndicator.visibility = View.GONE
                    }
                }
            }
        }

        binding.btnToLogin.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun inputListener(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkFieldsForErrors()
            }
            override fun afterTextChanged(s: Editable?) {}
        }
    }

    private fun checkFieldsForErrors() {
        val usernameError = binding.edRegisterUsername.error != null || binding.edRegisterUsername.text.isNullOrBlank()
        val emailError = binding.edRegisterEmail.error != null || binding.edRegisterEmail.text.isNullOrBlank()
        val passwordError = binding.edRegisterPassword.error != null || binding.edRegisterPassword.text.isNullOrBlank()

        val isEnabled = !emailError && !passwordError && !usernameError
        binding.btnRegister.isEnabled = isEnabled

        if (isEnabled) {
            binding.btnRegister.setBackgroundResource(R.drawable.rounded_button)
        } else {
            binding.btnRegister.setBackgroundResource(R.drawable.rounded_disabled_button)
        }
    }

    private fun playAnimation() {
        val logo = ObjectAnimator.ofFloat(binding.ivLogo, View.ALPHA, 1f).setDuration(100)
        val tvPageTitle = ObjectAnimator.ofFloat(binding.tvPageTitle, View.ALPHA, 1f).setDuration(100)
        val tvWelcome1 = ObjectAnimator.ofFloat(binding.tvWelcome1, View.ALPHA, 1f).setDuration(100)
        val usernameEditTextLayout = ObjectAnimator.ofFloat(binding.usernameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val btnRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(100)
        val tvRegister1 = ObjectAnimator.ofFloat(binding.tvRegister1, View.ALPHA, 1f).setDuration(100)
        val btnToLogin = ObjectAnimator.ofFloat(binding.btnToLogin, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                logo,
                tvPageTitle,
                tvWelcome1,
                usernameEditTextLayout,
                emailEditTextLayout,
                passwordEditTextLayout,
                btnRegister,
                tvRegister1,
                btnToLogin
            )
            startDelay = 100
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}