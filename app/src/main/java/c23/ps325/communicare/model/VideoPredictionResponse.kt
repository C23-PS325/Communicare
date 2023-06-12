package c23.ps325.communicare.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoPredictionResponse(

	@field:SerializedName("response")
	val data: Prediction,

	@field:SerializedName("error")
	val error: String,

	@field:SerializedName("message")
	val message: String
) : Parcelable

@Parcelize
data class FramesPrediction(

	@field:SerializedName("surprise")
	val surprise: Double,

	@field:SerializedName("sad")
	val sad: Double,

	@field:SerializedName("happy")
	val happy: Double,

	@field:SerializedName("angry")
	val angry: Double,

	@field:SerializedName("fear")
	val fear: Double
) : Parcelable {
	// iterator
	operator fun iterator(): Iterator<Pair<String, Double>> {
		return listOf("Angry" to angry, "Sad" to sad, "Surprise" to surprise, "Happy" to happy, "Fear" to fear).iterator()
	}
}

@Parcelize
data class Prediction(

	@field:SerializedName("frames")
	val frames: FramesPrediction,

	@field:SerializedName("audio")
	val audio: String
):Parcelable
