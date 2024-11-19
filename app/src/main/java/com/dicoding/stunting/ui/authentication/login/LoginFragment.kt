package com.dicoding.stunting.ui.authentication.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.dicoding.stunting.R
import com.dicoding.stunting.data.pref.UserModel
import com.dicoding.stunting.data.remote.Result
import com.dicoding.stunting.databinding.FragmentLoginBinding
import com.dicoding.stunting.ui.ViewModelFactory
import com.dicoding.stunting.ui.authentication.register.RegisterFragment
import com.dicoding.stunting.ui.main.MainActivity

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playAnimation()
        checkFieldsForErrors()
        setupAction()
    }

    private fun setupAction () {
        binding.edLoginEmail.addTextChangedListener(inputListener())
        binding.edLoginPassword.addTextChangedListener(inputListener())

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            loginViewModel.login(email, password).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressIndicator.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        val loginResponse = result.data
                        val userModel = UserModel(
                            userId = loginResponse.loginResult?.userId.toString(),
                            name = loginResponse.loginResult?.name.toString(),
                            email = email,
                            token = loginResponse.loginResult?.token.toString(),
                            isLogin = true
                        )
                        loginViewModel.saveSession(userModel)

                        AlertDialog.Builder(requireContext()).apply {
                            setTitle(R.string.login_success_alert_title)
                            setMessage(loginResponse.message)
                            setPositiveButton(R.string.success_alert_reply) { _, _ ->
                                val intent = Intent(requireContext(), MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            }
                            create()
                            show()
                        }
                        binding.progressIndicator.visibility = View.GONE
                    }
                    is Result.Error -> {
                        AlertDialog.Builder(requireContext()).apply {
                            setTitle(R.string.login_error_alert_title)
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

        binding.btnToSignup.setOnClickListener {
            parentFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.authentication_activity, RegisterFragment(),  RegisterFragment::class.java.simpleName)
            }
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
        val emailError = binding.edLoginEmail.error != null || binding.edLoginEmail.text.isNullOrBlank()
        val passwordError = binding.edLoginPassword.error != null || binding.edLoginPassword.text.isNullOrBlank()

        val isEnabled = !emailError && !passwordError
        binding.btnLogin.isEnabled = isEnabled

        if (isEnabled) {
            binding.btnLogin.setBackgroundResource(R.drawable.rounded_button)
        } else {
            binding.btnLogin.setBackgroundResource(R.drawable.rounded_disabled_button)
        }
    }

    private fun playAnimation() {
        val logo = ObjectAnimator.ofFloat(binding.ivLogo, View.ALPHA, 1f).setDuration(100)
        val tvNourish =
            ObjectAnimator.ofFloat(binding.tvNourish, View.ALPHA, 1f).setDuration(100)
        val tvWelcome1 =
            ObjectAnimator.ofFloat(binding.tvWelcome1, View.ALPHA, 1f).setDuration(100)
        val tvWelcome2 =
            ObjectAnimator.ofFloat(binding.tvWelcome2, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(100)
        val tvRegister1 = ObjectAnimator.ofFloat(binding.tvRegister1, View.ALPHA, 1f).setDuration(100)
        val btnToRegister = ObjectAnimator.ofFloat(binding.btnToSignup, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                logo,
                tvNourish,
                tvWelcome1,
                tvWelcome2,
                emailEditTextLayout,
                passwordEditTextLayout,
                btnLogin,
                tvRegister1,
                btnToRegister
            )
            startDelay = 100
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}