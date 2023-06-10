package c23.ps325.communicare.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import c23.ps325.communicare.model.VideoPredictionResponse
import c23.ps325.communicare.network.ServiceMLApi
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class VideoPredictRepository @Inject constructor(private val api: ServiceMLApi) {

    private val doUpload : MutableLiveData<VideoPredictionResponse?> = MutableLiveData()
    fun doUploadObserver() : LiveData<VideoPredictionResponse?> = doUpload

    private val _loading = MutableLiveData<Boolean>()
    fun isLoading(): LiveData<Boolean> = _loading

    fun uploadVideo(video:MultipartBody.Part){
        _loading.postValue(true)
        api.uploadVideo(video).enqueue(object : Callback<VideoPredictionResponse>{
            override fun onResponse(
                call: Call<VideoPredictionResponse>,
                response: Response<VideoPredictionResponse>
            ) {
                _loading.postValue(false)
                if (response.isSuccessful){
                    val body = response.body()
                    if (body != null){
                        doUpload.postValue(body)
                        Log.i(TAG, "onResponse: Success Upload Video")
                    }else{
                        doUpload.postValue(null)
                        Log.e(TAG, "onResponse: Failed to Upload Video, ${response.body()?.message}")
                    }
                }else{
                    doUpload.postValue(null)
                    Log.e(TAG, "onResponse: Failed Response, ${response.body()?.message}, ${response.code()}")
                }
            }

            override fun onFailure(call: Call<VideoPredictionResponse>, t: Throwable) {
                _loading.postValue(false)
                doUpload.postValue(null)
                Log.e(TAG, "onFailure: ${t.message}", t)
            }

        })
    }
}