package c23.ps325.communicare.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import c23.ps325.communicare.model.DataItem
import c23.ps325.communicare.model.ResponseScript
import c23.ps325.communicare.model.TextScript
import c23.ps325.communicare.network.ServiceScriptApi
import com.google.gson.Gson
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ScriptRepository @Inject constructor(private val api : ServiceScriptApi) {
    private val _script : MutableLiveData<List<String>?> = MutableLiveData()
    fun scriptObserver() : LiveData<List<String>?> = _script

    fun getScript(id : Int){
        api.getById(id).enqueue(object : Callback<ResponseScript>{
            override fun onResponse(
                call: Call<ResponseScript>,
                response: Response<ResponseScript>
            ) {
                val body = response.body()
                if (response.isSuccessful){
                    if (body != null){
                        val result = Gson().fromJson(body.data[0].textArray, Array<String>::class.java).toList()
                        _script.postValue(result)
                        Log.i("Success", "onResponse: Load Script")
                    }else{
                        _script.postValue(null)
                        Log.e("Failed", "onResponse: Load Script")
                    }
                }else{
                    _script.postValue(null)
                    Log.e("Failed", "onResponse: Failed Response")
                }
            }

            override fun onFailure(call: Call<ResponseScript>, t: Throwable) {
                _script.postValue(null)
                Log.e("Failed", "onFailure: ${t.message}", t)
            }

        })
    }
}