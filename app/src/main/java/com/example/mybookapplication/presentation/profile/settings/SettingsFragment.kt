package com.example.mybookapplication.presentation.profile.settings

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mybookapplication.R
import com.example.mybookapplication.databinding.FragmentSettingsBinding
import com.example.mybookapplication.domain.entity.UserProfile
import com.example.mybookapplication.domain.util.Event
import com.example.mybookapplication.presentation.profile.settings.editprofile.EditProfileActivity
import com.example.mybookapplication.presentation.signin.SignInActivity
import kotlinx.coroutines.launch

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel : SettingsViewModel by viewModels { SettingsViewModel.settingsViewModelFactory }
    private lateinit var user: UserProfile

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
        observeUserProfile()
    }

    private fun bindViews() {
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

    private fun observeUserProfile() {
        lifecycleScope.launch {
            viewModel.userProfile.collect {
                when(it) {
                    is Event.Success -> {
                        user = it.data
                        setUpProfile(user)
                    }
                    is Event.Failure -> {

                    }
                }
            }
        }
    }

    private fun setUpProfile(user: UserProfile) {
        binding.email.text = user.email
        binding.userName.text = user.username
    }

    private fun signOut() {
        viewModel.signOut()
        moveToSignInScreen()
    }

    private fun moveToSignInScreen() {
        startActivity(Intent(requireActivity(), SignInActivity::class.java))
        activity?.finish()
    }

    private val editProfileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.fetchUserProfile()
            observeUserProfile()
        }
    }

    private fun moveToEditProfileScreen(user : UserProfile) {
        val intent = Intent(requireActivity(), EditProfileActivity::class.java)
        intent.putExtra(USER_PROFILE_KEY, user)
        editProfileLauncher.launch(intent)
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

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
        Log.d("onDestroy", "onDestroy")
    }


}