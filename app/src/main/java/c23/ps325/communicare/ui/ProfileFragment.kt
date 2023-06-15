package c23.ps325.communicare.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import c23.ps325.communicare.R
import c23.ps325.communicare.databinding.FragmentHomeBinding
import c23.ps325.communicare.databinding.FragmentProfileBinding
import c23.ps325.communicare.viewmodel.AuthViewModel
import c23.ps325.communicare.viewmodel.DataStoreViewModel
import com.bumptech.glide.Glide
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
            dataStoreViewModel.logout()
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
                        Glide.with(requireView()).load(data.photoUrl).into(userPhoto)
                    }
                }
            }
        }
    }
}