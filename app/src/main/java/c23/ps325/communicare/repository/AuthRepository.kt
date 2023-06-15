package c23.ps325.communicare.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import c23.ps325.communicare.model.*
import c23.ps325.communicare.network.ServiceApi
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(private val api: ServiceApi){

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

    private val _dataEdit : MutableLiveData<EditUserResponse?> = MutableLiveData()
    fun dataEditObserver() : LiveData<EditUserResponse?> = _dataEdit

    fun patchEditUser(userData : String, username: String, email: String, password: String, photoUrl : String){
        api.editUser(userData,EditUserRequest(username,password, email, photoUrl)).enqueue(object : Callback<EditUserResponse>{
            override fun onResponse(
                call: Call<EditUserResponse>,
                response: Response<EditUserResponse>
            ) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body != null){
                        _dataEdit.postValue(body)
                        Log.i("Success", "onResponse: Edit User")
                    }else{
                        _dataEdit.postValue(null)
                        Log.e("Fail", "onResponse: Edit User")
                    }
                }else{
                    _dataEdit.postValue(null)
                    Log.e("Failed", "onResponse: Edit User")
                }
            }

            override fun onFailure(call: Call<EditUserResponse>, t: Throwable) {
                _dataEdit.postValue(null)
                Log.e("Failure", "onFailure: Edit User", t)
            }

        })
    }

    private val _user : MutableLiveData<DataUserItem?> = MutableLiveData()
    fun userObserver() : LiveData<DataUserItem?> = _user
    fun getUser(username: String){
        api.getByUsername(username).enqueue(object : Callback<ResponseGetUser>{
            override fun onResponse(
                call: Call<ResponseGetUser>,
                response: Response<ResponseGetUser>
            ) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body != null){
                        _user.postValue(body.data[0])
                        Log.i("Success", "onResponse: Get User")
                    }else{
                        _user.postValue(null)
                        Log.e("Fail", "onResponse: Get User")
                    }
                }else{
                    _user.postValue(null)
                    Log.e("Failed", "onResponse: Get User")
                }
            }

            override fun onFailure(call: Call<ResponseGetUser>, t: Throwable) {
                _user.postValue(null)
                Log.e("Failure", "onFailure: ${t.message}", t)
            }

        })
    }

}