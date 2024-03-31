package com.example.mybookapplication.presentation.profile.settings.editprofile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mybookapplication.R
import com.example.mybookapplication.databinding.ActivityEditProfileBinding
import com.example.mybookapplication.domain.entity.UserProfile
import com.example.mybookapplication.presentation.profile.settings.SettingsFragment

class EditProfileActivity: AppCompatActivity(R.layout.activity_edit_profile) {
    private lateinit var binding : ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        bindViews()
    }

    private fun bindViews() {
        val user = getUserProfile()
        binding.editUsername.setText(user?.username)
        binding.email.text = user?.email
    }

    private fun getUserProfile(): UserProfile? {
        return intent.getParcelableExtra(SettingsFragment.USER_PROFILE_KEY)
    }
}