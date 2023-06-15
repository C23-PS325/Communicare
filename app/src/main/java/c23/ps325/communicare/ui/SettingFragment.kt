package c23.ps325.communicare.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import c23.ps325.communicare.R
import c23.ps325.communicare.databinding.FragmentSettingBinding
import c23.ps325.communicare.viewmodel.AuthViewModel
import c23.ps325.communicare.viewmodel.DataStoreViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private val authViewModel : AuthViewModel by viewModels()
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        setDataUser()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        binding.btnEditProfile.setOnClickListener {
            editUser()
        }

    }

    private fun setDataUser() {
        dataStoreViewModel.getName().observe(viewLifecycleOwner){ username ->
            authViewModel.getUser(username)
            authViewModel.userObserver().observe(viewLifecycleOwner){
                val data = it
                if (data != null) {
                    binding.apply {
                        inputEmail.setText(data.email)
                        inputUsername.setText(data.username)
                        inputPassword.setText(data.password)
                        inputImage.setText(data.photoUrl)
                        Glide.with(requireView()).load(data.photoUrl).placeholder(R.drawable.dummy_profile).into(userPhoto)
                    }
                }
            }
        }
    }


    private fun editUser() {
        dataStoreViewModel.getName().observe(viewLifecycleOwner){ userData ->
            if (userData != null){
                val username = binding.inputUsername.text.toString()
                val password = binding.inputPassword.text.toString()
                val email = binding.inputEmail.text.toString()
                val photo = binding.inputImage.text.toString()

                authViewModel.patchUser(userData, username, email, password, photo)
                authViewModel.patchObserver().observe(viewLifecycleOwner){
                    if (it != null){
                        Navigation.findNavController(requireView()).navigate(R.id.action_settingFragment_to_onBoardingFragment)
                        dataStoreViewModel.logout()
                        Toast.makeText(context, "Please Login again", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context, "Failed Update User", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}