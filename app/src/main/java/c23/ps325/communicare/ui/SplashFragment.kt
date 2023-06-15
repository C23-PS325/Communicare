package c23.ps325.communicare.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import c23.ps325.communicare.R
import c23.ps325.communicare.databinding.FragmentSplashBinding
import c23.ps325.communicare.viewmodel.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding : FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val dataStoreViewModel : DataStoreViewModel  by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val splashTime : Long = 2000

        Handler(Looper.myLooper()!!).postDelayed({
            dataStoreViewModel.getStatus().observe(viewLifecycleOwner){
                if(it){
                    Navigation.findNavController(requireView()).navigate(R.id.action_splashFragment_to_homeFragment)
                }else{
                    Navigation.findNavController(requireView()).navigate(R.id.action_splashFragment_to_onBoardingFragment)
                }
            }
        }, splashTime)
    }
}