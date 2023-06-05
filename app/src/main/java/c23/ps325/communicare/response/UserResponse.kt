package c23.ps325.communicare.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("photoUrl")
	val photoUrl: String?,

	@field:SerializedName("password")
	val password: String?,

	@field:SerializedName("id")
	val id: Int?,

	@field:SerializedName("email")
	val email: String?,

	@field:SerializedName("username")
	val username: String?
)
