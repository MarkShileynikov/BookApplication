package com.example.mybookapplication.presentation.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mybookapplication.databinding.ActivitySignupBinding
import com.example.mybookapplication.presentation.bottomnavigation.MainActivity
import com.example.mybookapplication.presentation.signin.SignInActivity
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding
    private val viewModel : SignUpViewModel by viewModels { SignUpViewModel.signUpViewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setUpViews()
        observeEvents()
    }

    private fun setUpViews() {
        binding.signUp.setOnClickListener {
            val email = binding.emailView.text.toString()
            val password = binding.passwordView.text.toString()
            val username = binding.usernameView.text.toString()
            try {
                viewModel.onSignUpButtonClicked(
                    email = email,
                    password = password,
                    username = username,
                    context = this
                )
            } catch (e: Exception) {
                binding.error.visibility = View.VISIBLE
                binding.error.text = e.message
            }

        }
        binding.signIn.setOnClickListener {
            moveToSignInScreen()
        }
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    when(it) {
                        is SignUpViewState.Success -> {
                            moveToMainScreen()
                        }
                        is SignUpViewState.Loading -> {
                            binding.signUp.isEnabled = false
                        }
                        is SignUpViewState.Failure -> {
                            binding.signUp.isEnabled = true
                            binding.error.visibility = View.VISIBLE
                            binding.error.text = it.message
                        }
                        is SignUpViewState.Idle -> {}
                    }
                }
            }
        }
    }
    private fun moveToSignInScreen() {
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }
    private fun moveToMainScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}