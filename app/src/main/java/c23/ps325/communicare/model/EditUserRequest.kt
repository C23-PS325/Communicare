package c23.ps325.communicare.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class EditUserRequest(

	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
) : Parcelable
