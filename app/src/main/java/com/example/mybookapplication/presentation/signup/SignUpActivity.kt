package com.example.mybookapplication.presentation.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mybookapplication.R
import com.example.mybookapplication.databinding.ActivitySigninBinding
import com.example.mybookapplication.databinding.ActivitySignupBinding
import com.example.mybookapplication.presentation.bottomnavigation.MainActivity
import com.example.mybookapplication.presentation.signin.SignInActivity
import com.example.mybookapplication.presentation.signin.SignInViewModel
import com.example.mybookapplication.presentation.signin.SignInViewState
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
            viewModel.onSignUpButtonClicked(
                email = binding.emailView.text.toString(),
                password = binding.passwordView.text.toString(),
                username = binding.usernameView.text.toString()
            )
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
                            Toast.makeText(this@SignUpActivity, it.message, Toast.LENGTH_SHORT).show()
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