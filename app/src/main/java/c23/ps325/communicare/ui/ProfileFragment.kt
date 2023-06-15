package c23.ps325.communicare.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import c23.ps325.communicare.R
import c23.ps325.communicare.databinding.FragmentProfileBinding
import c23.ps325.communicare.viewmodel.AuthViewModel
import c23.ps325.communicare.viewmodel.DataStoreViewModel
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val dataStoreViewModel : DataStoreViewModel  by viewModels()
    private val authViewModel : AuthViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setting.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_settingFragment)
        }

        binding.logout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.logout))
                .setMessage(resources.getString(R.string.are_you_sure))
                .setCancelable(false)
                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                    // Respond to negative button press
                    dialog.cancel()
                }
                .setPositiveButton(resources.getString(R.string.logout)) { _, _ ->
                    // Respond to positive button press
                    dataStoreViewModel.logout()
                    Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_onBoardingFragment)
                }
                .show()

        }

        binding.btnBack.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_homeFragment)
        }

        dataStoreViewModel.getName().observe(viewLifecycleOwner) { name ->
            binding.userName.text = name
        }

        dataStoreViewModel.getName().observe(viewLifecycleOwner){ username ->
            authViewModel.getUser(username)
            authViewModel.userObserver().observe(viewLifecycleOwner){
                val data = it
                if (data != null) {
                    binding.apply {
                        Glide.with(requireView()).load(data.photoUrl).placeholder(R.drawable.dummy_profile).into(userPhoto)
                    }
                }
            }
        }
    }
}