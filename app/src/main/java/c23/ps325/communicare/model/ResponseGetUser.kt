package c23.ps325.communicare.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ResponseGetUser(

	@field:SerializedName("data")
	val data: List<DataUserItem>,

	@field:SerializedName("message")
	val message: String
) : Parcelable

@Parcelize
data class DataUserItem(

	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
) : Parcelable
