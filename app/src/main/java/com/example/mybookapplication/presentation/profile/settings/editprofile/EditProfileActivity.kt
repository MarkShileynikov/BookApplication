package com.example.mybookapplication.presentation.profile.settings.editprofile

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import coil.load
import coil.transform.CircleCropTransformation
import com.example.mybookapplication.R
import com.example.mybookapplication.databinding.ActivityEditProfileBinding
import com.example.mybookapplication.domain.entity.UserProfile
import com.example.mybookapplication.presentation.profile.settings.SettingsFragment
import com.example.mybookapplication.presentation.util.ViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileActivity: AppCompatActivity(R.layout.activity_edit_profile) {
    private val viewModel : EditProfileViewModel by viewModels()
    private lateinit var binding : ActivityEditProfileBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var requestGalleryLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        bindViews()
        observeUsername()
        setUpOpenGalleryLauncher()
    }

    private fun bindViews() {
        val user = getUserProfile()
        binding.editUsername.setText(user?.username)
        binding.email.text = user?.email

        if (user?.avatar != "") {
            binding.avatar.load(user?.avatar) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }
        binding.savetUsernameButton.setOnClickListener {
            val newUsername = binding.editUsername.text.toString()
            if (user != null && newUsername.isNotBlank()) {
                viewModel.updateUsername(user.userId, newUsername)
            } else {
                binding.error.visibility = View.VISIBLE
                binding.error.text = getString(R.string.fill_field)
            }
        }
        binding.editAvatarButton.setOnClickListener {
            if (checkGalleryPermission()) {
                openGallery()
            } else {
                requestGalleryPermission()
            }
        }

    }

    private fun getUserProfile(): UserProfile? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(SettingsFragment.USER_PROFILE_KEY, UserProfile::class.java)
        } else {
            intent.getParcelableExtra(SettingsFragment.USER_PROFILE_KEY)
        }
    }

    private fun observeUsername() {
        viewModel.viewModelScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.updateUsernameViewState.collect {
                    when(it) {
                        is ViewState.Success -> {
                            handleOnSuccess()
                        }
                        is ViewState.Loading -> {
                            binding.error.isEnabled = false
                        }
                        is ViewState.Failure -> {
                            handleOnFailure(it.message)
                        }
                    }
                }
            }
        }
    }

    private fun handleOnSuccess() {
        setResult(Activity.RESULT_OK)
        binding.error.isEnabled = true
        val message = binding.error
        message.visibility = View.VISIBLE
        message.setTextColor(getColor(R.color.text_color))
        message.text = getString(R.string.username_successfully_updated)
    }

    private fun handleOnFailure(message: String) {
        binding.error.isEnabled = true
        binding.error.visibility = View.VISIBLE
        binding.error.text = message
    }

    private fun setUpOpenGalleryLauncher() {
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {isGranted ->
            if (isGranted){
                openGallery()
            }
        }
        requestGalleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {uri ->
                binding.avatar.load(uri) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
                viewModel.updateAvatar(uri, getUserProfile()?.userId ?: "")
                observeAvatar()
            }
        }
    }

    private fun checkGalleryPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun openGallery() {
        requestGalleryLauncher.launch("image/*")
    }

    private fun requestGalleryPermission() {
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun observeAvatar() {
        viewModel.viewModelScope.launch {
            viewModel.updateAvatarViewState.collect {
                when(it) {
                    is ViewState.Success -> {
                        handleOnAvatarUpdateSuccess()
                    }
                    is ViewState.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                        binding.avatar.visibility = View.GONE
                    }
                    is ViewState.Failure -> {
                        Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun handleOnAvatarUpdateSuccess() {
        binding.loading.visibility = View.GONE
        binding.avatar.visibility = View.VISIBLE
    }
}