package c23.ps325.communicare.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PredictionHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val angry : Double,
    val disgust : Double,
    val fear : Double,
    val happy : Double,
    val sad : Double,
    val surprise : Double,
    val soundEmotion : String,
    val date : String
)
