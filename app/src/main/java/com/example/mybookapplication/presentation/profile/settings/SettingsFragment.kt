package com.example.mybookapplication.presentation.profile.settings

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mybookapplication.R
import com.example.mybookapplication.databinding.FragmentSettingsBinding
import com.example.mybookapplication.domain.entity.UserProfile
import com.example.mybookapplication.presentation.profile.ProfileFragment
import com.example.mybookapplication.presentation.profile.settings.editprofile.EditProfileActivity
import com.example.mybookapplication.presentation.signin.SignInActivity

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel : SettingsViewModel by viewModels { SettingsViewModel.settingsViewModelFactory }

    companion object {
        const val USER_PROFILE_KEY = "USER_PROFILE_KEY"
    }
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
        val user = getUserProfile()
        binding.email.text = user?.email
        binding.userName.text = user?.username
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.signOut.setOnClickListener {
            showAlertDialog()
        }
        binding.userCard.setOnClickListener {
            moveToEditProfileScreen(user)
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

    private fun moveToEditProfileScreen(user : UserProfile?) {
        val intent = Intent(requireActivity(), EditProfileActivity::class.java)
        intent.putExtra(USER_PROFILE_KEY, user)
        startActivity(intent)
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialog)
        builder.apply {
            setTitle(getString(R.string.sign_out_alert))
            setPositiveButton(getString(R.string.ok)) {dialog, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    signOut()
                    dialog.dismiss()
                }
            }
            setNegativeButton(getString(R.string.cancel)) {dialog, which ->
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    dialog.dismiss()
                }
            }

            val dialog = builder.create()

            dialog.show()

        }
    }
}