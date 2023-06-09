package c23.ps325.communicare.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CommunicareDAO {
    @Query("SELECT * FROM PredictionHistory ORDER BY date DESC")
    fun getAllHistory(): LiveData<List<PredictionHistory>>

    @Insert
    fun addHistory(history: PredictionHistory)

    @Delete
    fun deleteHistory(history: PredictionHistory)
}