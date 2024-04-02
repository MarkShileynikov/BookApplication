package com.example.mybookapplication.presentation.signup

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mybookapplication.App
import com.example.mybookapplication.R
import com.example.mybookapplication.data.api.NetworkClientConfig
import com.example.mybookapplication.data.prefs.PrefsDataSourceImpl
import com.example.mybookapplication.data.repository.SessionRepositoryImpl
import com.example.mybookapplication.domain.usecase.SignInUseCase
import com.example.mybookapplication.domain.usecase.SignUpUseCase
import com.example.mybookapplication.presentation.util.isConnectedToNetwork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    context : Application,
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase
) : AndroidViewModel(context) {
    val viewState = MutableStateFlow<SignUpViewState>(SignUpViewState.Idle)

    fun onSignUpButtonClicked(email: String, password : String, username : String, context: Context) {
        when {
            !isConnectedToNetwork(context) -> {
                showMessage(context)
            }
            email.isBlank() || password.isBlank() || username.isBlank() -> {
                throw Exception(context.getString(R.string.fill_all_fields))
            }
            !isPasswordValid(password) -> {
                throw Exception(context.getString(R.string.password_is_not_valid))
            }
            else -> {
                viewModelScope.launch {
                    signUpUseCase(SignUpUseCase.Params(email, password, username))
                        .onStart { viewState.value = SignUpViewState.Loading }
                        .catch {
                            viewState.value =
                                SignUpViewState.Failure(it.message ?: "Something went wrong.")
                        }
                        .collect { _ ->
                            signIn(email, password)
                        }
                }
            }
        }

    }

    private fun showMessage(context: Context) {
        Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
    }

    private suspend fun signIn(email: String, password: String) {
        viewModelScope.launch {
            signInUseCase(SignInUseCase.Params(email, password))
                .onStart { viewState.value = SignUpViewState.Loading }
                .catch { viewState.value = SignUpViewState.Failure(it.message ?: "Something went wrong.") }
                .collect {_ ->
                    viewState.value = SignUpViewState.Success
                }
        }
    }

    private fun isPasswordValid(password: String) : Boolean{
        val passwordRegex = Regex("^(?=.*[A-Za-z]).{8,}\$")
        return passwordRegex.matches(password)
    }
}