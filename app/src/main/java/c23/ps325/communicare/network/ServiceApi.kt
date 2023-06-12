package c23.ps325.communicare.network

import c23.ps325.communicare.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ServiceApi {
//    Auth Endpoint
    @POST("/users")
    suspend fun register(@Body registerRequest: RegisterRequest): UserResponse

    @POST("/users/:username")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
}

interface ServiceMLApi {
    @POST("predict-video")
    @Multipart
//    @Headers("Content-Type: multipart/form-data")
    fun uploadVideo(
        @Part file_video : MultipartBody.Part
    ): Call<VideoPredictionResponse>
}