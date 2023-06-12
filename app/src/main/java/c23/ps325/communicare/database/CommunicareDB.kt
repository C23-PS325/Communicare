package c23.ps325.communicare.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PredictionHistory::class], version = 1 )
abstract class CommunicareDB : RoomDatabase() {
    abstract fun communicareDAO(): CommunicareDAO
}