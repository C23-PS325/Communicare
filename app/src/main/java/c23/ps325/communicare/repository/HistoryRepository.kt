package c23.ps325.communicare.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import c23.ps325.communicare.database.CommunicareDAO
import c23.ps325.communicare.database.PredictionHistory
import javax.inject.Inject

class HistoryRepository @Inject constructor(val dao : CommunicareDAO) {

    fun getAllHistory() : LiveData<List<PredictionHistory>> {
        return dao.getAllHistory()
    }

    fun insertHistory(history : PredictionHistory) {
        dao.addHistory(history)
    }

    fun deleteHistory(history : PredictionHistory) {
        dao.deleteHistory(history)
    }
}