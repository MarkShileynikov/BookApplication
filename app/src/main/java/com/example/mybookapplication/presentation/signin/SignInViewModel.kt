package com.example.mybookapplication.presentation.signin

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybookapplication.R
import com.example.mybookapplication.domain.usecase.SignInUseCase
import com.example.mybookapplication.presentation.util.isConnectedToNetwork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    context : Application,
    private val signInUseCase: SignInUseCase
) : AndroidViewModel(context) {
    val viewState = MutableStateFlow<SignInViewState>(SignInViewState.Idle)

    fun onSignInButtonClicked(email: String, password : String, context: Context) {
        if (!isConnectedToNetwork(context)) {
            showMessage(context)
        } else {
            viewModelScope.launch {
                signInUseCase(SignInUseCase.Params(email, password))
                    .onStart { viewState.value = SignInViewState.Loading }
                    .catch {
                        viewState.value =
                            SignInViewState.Failure(it.message ?: "Something went wrong.")
                    }
                    .collect {
                        viewState.value = SignInViewState.Success
                    }
            }
        }
    }

    private fun showMessage(context: Context) {
        Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
    }
}