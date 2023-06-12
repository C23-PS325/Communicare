package c23.ps325.communicare.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ResponseScript(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("message")
	val message: String
) : Parcelable

@Parcelize
data class DataItem(

	@field:SerializedName("starter")
	val starter: String,

	@field:SerializedName("case_name")
	val caseName: String,

	@field:SerializedName("text_array")
	val textArray: String,

	@field:SerializedName("id")
	val id: Int
) : Parcelable

@Parcelize
data class TextScript(
	@field:SerializedName("text")
	val textScript : String
) : Parcelable