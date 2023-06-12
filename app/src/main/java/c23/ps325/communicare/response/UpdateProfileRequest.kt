package c23.ps325.communicare.response

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(

    @SerializedName("photoUrl")
    val photoUrl: String?,

    @SerializedName("newPassword")
    val newPassword: String?
)

