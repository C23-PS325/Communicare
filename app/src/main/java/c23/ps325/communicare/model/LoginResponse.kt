package c23.ps325.communicare.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("data")
	val data: List<UserResponse>,

    @field:SerializedName("message")
	val message: String
)