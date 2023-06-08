package c23.ps325.communicare.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import c23.ps325.communicare.R
import c23.ps325.communicare.databinding.FragmentLoginBinding
import c23.ps325.communicare.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var viewModel: AuthViewModel
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        // Inisialisasi ViewModel menggunakan Hilt
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        // Observasi navigateToMain untuk pindah halaman jika login berhasil
        viewModel.navigate.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                // Pindah ke halaman utama (misalnya, menggunakan Navigation Component)
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }

        // Observasi errorMessage untuk menampilkan pesan error jika login gagal
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            // Tampilkan pesan error kepada pengguna (misalnya, menggunakan Toast)
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        // Set up login button click listener
        binding.btnLogin.setOnClickListener {
            val username = binding.inputName.text.toString()
            val password = binding.inputPassword.text.toString()

            // Panggil metode login dari ViewModel
            viewModel.login(username, password)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}