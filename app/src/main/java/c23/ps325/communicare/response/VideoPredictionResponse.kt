package c23.ps325.communicare.response

import com.google.gson.annotations.SerializedName

data class VideoPredictionResponse(

	@field:SerializedName("response")
	val data: Prediction,

	@field:SerializedName("error")
	val error: String,

	@field:SerializedName("message")
	val message: String
)

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
){
	// iterator
	operator fun iterator(): Iterator<Pair<String, Double>> {
		return listOf("Angry" to angry, "Sad" to sad, "Surprise" to surprise, "Happy" to happy, "Fear" to fear).iterator()
	}
}

data class Prediction(

	@field:SerializedName("frames")
	val frames: FramesPrediction,

	@field:SerializedName("audio")
	val audio: String
)
