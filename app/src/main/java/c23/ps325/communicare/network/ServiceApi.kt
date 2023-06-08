package c23.ps325.communicare.network

import c23.ps325.communicare.response.*
import retrofit2.Call
import retrofit2.http.*

interface ServiceApi {
//    Auth Endpoint
    @POST("/users")
    suspend fun register(@Body registerRequest: RegisterRequest): UserResponse

    @POST("/users/:username")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

//    ML Endpoint

}