package com.example.mybookapplication.presentation.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mybookapplication.databinding.ActivitySigninBinding
import com.example.mybookapplication.presentation.bottomnavigation.MainActivity
import com.example.mybookapplication.presentation.signup.SignUpActivity
import kotlinx.coroutines.launch

class SignInActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySigninBinding
    private val viewModel : SignInViewModel by viewModels { SignInViewModel.signInViewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setUpViews()
        observeEvents()
    }

    private fun setUpViews() {
        binding.registerButton.setOnClickListener {
            moveToSignUpScreen()
        }
        binding.signIn.setOnClickListener {
            viewModel.onSignInButtonClicked(
                email = binding.emailView.text.toString(),
                password = binding.passwordView.text.toString(),
                context = this
            )
        }
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    when(it) {
                        is SignInViewState.Success -> {
                            moveToMainScreen()
                        }
                        is SignInViewState.Loading -> {
                            binding.signIn.isEnabled = false
                        }
                        is SignInViewState.Failure -> {
                            binding.signIn.isEnabled = true
                            binding.error.visibility = View.VISIBLE
                            binding.error.text = it.message
                        }
                        is SignInViewState.Idle -> {}
                    }
                }
            }
        }
    }
    private fun moveToSignUpScreen() {
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }
    private fun moveToMainScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}