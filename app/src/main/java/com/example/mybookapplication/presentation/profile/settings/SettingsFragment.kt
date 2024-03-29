package com.example.mybookapplication.presentation.profile.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mybookapplication.R
import com.example.mybookapplication.databinding.FragmentSettingsBinding
import com.example.mybookapplication.domain.entity.UserProfile
import com.example.mybookapplication.presentation.profile.ProfileFragment
import com.example.mybookapplication.presentation.signin.SignInActivity

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel : SettingsViewModel by viewModels { SettingsViewModel.settingsViewModelFactory }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
    }

    private fun bindViews() {
        binding.email.text = getUserProfile()?.email ?: ""
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.signOut.setOnClickListener {
            signOut()
        }
    }

    private fun getUserProfile() : UserProfile? {
        return arguments?.getParcelable(ProfileFragment.USER_PROFILE_KEY)
    }
    private fun signOut() {
        viewModel.signOut()
        moveToSignInScreen()
    }
    private fun moveToSignInScreen() {
        startActivity(Intent(requireActivity(), SignInActivity::class.java))
        activity?.finish()
    }
}