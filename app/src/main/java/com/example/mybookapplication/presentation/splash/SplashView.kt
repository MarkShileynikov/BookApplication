package com.example.mybookapplication.presentation.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mybookapplication.R
import com.example.mybookapplication.presentation.bottomnavigation.MainActivity
import com.example.mybookapplication.presentation.signin.SignInActivity
import kotlinx.coroutines.launch

class SplashView : AppCompatActivity(R.layout.activity_splash) {
    private val viewModel: SplashViewModel by viewModels { SplashViewModel.splashViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeSplashState()
    }

    private fun observeSplashState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    when(it) {
                        is SplashViewState.Success -> {
                            startActivity(Intent(this@SplashView, MainActivity::class.java))
                            finish()
                        }
                        is SplashViewState.Failure -> {
                            startActivity(Intent(this@SplashView, SignInActivity::class.java))
                            finish()
                        }
                        is SplashViewState.Loading -> {

                        }
                    }
                }
            }
        }
    }
}