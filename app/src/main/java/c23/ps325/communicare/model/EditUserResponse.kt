package c23.ps325.communicare.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class EditUserResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String
) : Parcelable

@Parcelize
data class Data(

	@field:SerializedName("fieldCount")
	val fieldCount: Int,

	@field:SerializedName("serverStatus")
	val serverStatus: Int,

	@field:SerializedName("protocol41")
	val protocol41: Boolean,

	@field:SerializedName("changedRows")
	val changedRows: Int,

	@field:SerializedName("affectedRows")
	val affectedRows: Int,

	@field:SerializedName("warningCount")
	val warningCount: Int,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("insertId")
	val insertId: Int
) : Parcelable
