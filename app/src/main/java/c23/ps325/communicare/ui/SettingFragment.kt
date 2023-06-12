package c23.ps325.communicare.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import c23.ps325.communicare.R
import c23.ps325.communicare.databinding.FragmentSettingBinding
import c23.ps325.communicare.viewmodel.AuthViewModel
import c23.ps325.communicare.viewmodel.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import c23.ps325.communicare.repository.Result
import com.bumptech.glide.Glide

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private val viewModel: AuthViewModel by viewModels()
    private val REQUEST_CODE_PICK_IMAGE = 123
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnLanguange.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_profileFragment)
        }

        dataStoreViewModel.liveThemeSettings().observe(viewLifecycleOwner){ isDarkModeActive: Boolean ->
            if (isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.mode.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.mode.isChecked = false
            }
        }

        binding.mode.setOnCheckedChangeListener{_:CompoundButton?, isChecked: Boolean ->
            dataStoreViewModel.saveThemeSettings(isChecked)
        }

        binding.userPhoto.setOnClickListener {
            openGallery()
        }


        binding.btnEdit.setOnClickListener {
            val newPassword = binding.editPassword.text.toString()
            val userId = 1 // Replace with actual user ID
            viewModel.updateProfile(userId, null, newPassword)
        }

        // Observe updateProfileResult LiveData for the result
        viewModel.updateProfileResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Success -> {
                    // Handle success result
                    Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    // Handle error result
                    Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }
        })

        return view
    }

    @Suppress("DEPRECATION")
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { imageUri ->
                Glide.with(this)
                    .load(imageUri)
                    .into(binding.userPhoto)

                val newPhotoUrl = imageUri.toString()
                val userId = 1 // Replace with actual user ID
                viewModel.updateProfile(userId, newPhotoUrl, null)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}