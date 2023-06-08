package c23.ps325.communicare.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import c23.ps325.communicare.R
import c23.ps325.communicare.databinding.FragmentHomeBinding
import c23.ps325.communicare.databinding.FragmentProfileBinding
import c23.ps325.communicare.viewmodel.AuthViewModel
import c23.ps325.communicare.viewmodel.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var dataStoreViewModel: DataStoreViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        dataStoreViewModel = ViewModelProvider(this).get(DataStoreViewModel::class.java)

        binding.setting.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_settingFragment)
        }

        binding.logout.setOnClickListener {
            dataStoreViewModel.logout()
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
        }

        return view
    }
}