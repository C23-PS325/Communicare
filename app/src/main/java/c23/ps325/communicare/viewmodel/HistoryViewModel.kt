package c23.ps325.communicare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import c23.ps325.communicare.database.PredictionHistory
import c23.ps325.communicare.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val repo : HistoryRepository) : ViewModel() {

    fun getAllHistory() = repo.getAllHistory()

    fun addHistory(history : PredictionHistory) {
        viewModelScope.launch {
            repo.insertHistory(history)
        }
    }

    fun deleteHistory(history : PredictionHistory) {
        viewModelScope.launch {
            repo.deleteHistory(history)
        }
    }
}