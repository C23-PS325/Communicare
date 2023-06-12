package c23.ps325.communicare.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import c23.ps325.communicare.database.CommunicareDAO
import c23.ps325.communicare.database.PredictionHistory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class HistoryRepository @Inject constructor(val dao : CommunicareDAO) {
    private val executorService : ExecutorService = Executors.newSingleThreadExecutor()

    fun getAllHistory() : LiveData<List<PredictionHistory>> {
        return dao.getAllHistory()
    }

    fun insertHistory(history : PredictionHistory) {
        executorService.execute { dao.addHistory(history) }
    }

    fun deleteHistory(history : PredictionHistory) {
        executorService.execute { dao.deleteHistory(history) }
    }
}