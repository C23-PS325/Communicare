package c23.ps325.communicare.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import c23.ps325.communicare.R
import c23.ps325.communicare.databinding.FragmentHomeBinding
import c23.ps325.communicare.databinding.FragmentLoginBinding
import c23.ps325.communicare.viewmodel.AuthViewModel


class HomeFragment : Fragment() {

    private lateinit var dataStoreViewModel: AuthViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.userPhoto.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        return view
    }
}