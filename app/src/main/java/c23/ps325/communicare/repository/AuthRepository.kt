package c23.ps325.communicare.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import c23.ps325.communicare.network.ServiceApi
import c23.ps325.communicare.model.LoginRequest
import c23.ps325.communicare.model.LoginResponse
import c23.ps325.communicare.model.RegisterRequest
import c23.ps325.communicare.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(private val api: ServiceApi){
//    LiveData
    fun login(username: String, password: String): LiveData<Result<LoginResponse>> {
        val loginData = MutableLiveData<Result<LoginResponse>>()

        // Lakukan pemanggilan API login melalui ServiceApi
        val loginRequest = LoginRequest(username, password)
        val call = api.login(loginRequest)

        // Jalankan permintaan secara asynchronous
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        loginData.value = loginResponse.toSuccessResult()
                    } else {
                        loginData.value = errorResult("Login response is null")
                    }
                } else {
                    loginData.value = errorResult("Failed to login: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginData.value = errorResult("Failed to login: ${t.message}")
            }
        })

        return loginData
    }

    suspend fun register(username: String, email: String, password: String): Result<UserResponse> {
        val registerRequest = RegisterRequest(username, email, password)
        return try {
            val response = api.register(registerRequest)
            response.toSuccessResult()
        } catch (e: Exception) {
            errorResult(e.message)
        }
    }

//    Retrofit
}