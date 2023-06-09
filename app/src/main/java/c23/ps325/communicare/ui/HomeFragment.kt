package c23.ps325.communicare.ui

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import c23.ps325.communicare.database.PredictionHistory
import c23.ps325.communicare.databinding.FragmentHomeBinding
import c23.ps325.communicare.ui.adapter.HistoryAdapter
import c23.ps325.communicare.viewmodel.AuthViewModel
import c23.ps325.communicare.viewmodel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var dataStoreViewModel: AuthViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHistoryRecView()
    }

    private fun setupHistoryRecView(){
        historyViewModel.getAllHistory().observe(viewLifecycleOwner) {
            val dataHistory = it
            val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            val adapter = HistoryAdapter(dataHistory)
            binding.rvHistory.layoutManager = layoutManager
            binding.rvHistory.adapter = adapter
        }
        /**
         * This is a dummy data for testing purpose
         */
//        val dataHistory = listOf(PredictionHistory(0,59.2, 12.2, 10.0, 5.5, 0.2, 1.1, "Disgust", "2023-01-01"), PredictionHistory(0,59.2, 12.2, 10.0, 5.5, 0.2, 1.1, "Disgust", "2023-01-01"), PredictionHistory(0,59.2, 12.2, 10.0, 5.5, 0.2, 1.1, "Disgust", "2023-01-01"), PredictionHistory(0,59.2, 12.2, 10.0, 5.5, 0.2, 1.1, "Disgust", "2023-01-01"))
//        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        val adapter = HistoryAdapter(dataHistory)
//        binding.rvHistory.layoutManager = layoutManager
//        binding.rvHistory.adapter = adapter
    }

    companion object{
        /** Convenience method used to check if all permissions required by this app are granted */
//        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
//            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
//        }
    }

}
