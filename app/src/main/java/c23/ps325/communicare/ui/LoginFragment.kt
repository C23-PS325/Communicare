package c23.ps325.communicare.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import c23.ps325.communicare.R
import c23.ps325.communicare.databinding.FragmentLoginBinding
import c23.ps325.communicare.viewmodel.AuthViewModel
import c23.ps325.communicare.viewmodel.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var viewModel: AuthViewModel
    private lateinit var dataStoreViewModel : DataStoreViewModel
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var username : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        dataStoreViewModel = ViewModelProvider(this).get(DataStoreViewModel::class.java)

        viewModel.navigate.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                dataStoreViewModel.saveLogin(true, username)
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        binding.btnLogin.setOnClickListener {
            username = binding.inputName.text.toString()
            val password = binding.inputPassword.text.toString()

            viewModel.login(username, password)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}