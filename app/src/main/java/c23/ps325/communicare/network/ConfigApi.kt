package c23.ps325.communicare.network

import c23.ps325.communicare.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ConfigApi {

    @Singleton
    @Provides
    fun provideBaseUrl1(): ServiceApi {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
             HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
         } else {
             HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
         }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_AUTH)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ServiceApi::class.java)
    }
    @Singleton
    @Provides
    fun provideBaseUrl2(): ServiceMLApi {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val protocols = listOf(Protocol.HTTP_1_1, Protocol.HTTP_2)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .protocols(protocols)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_ML)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ServiceMLApi::class.java)
    }

    @Singleton
    @Provides
    fun provideBaseUrl3(): ServiceScriptApi {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val protocols = listOf(Protocol.HTTP_1_1, Protocol.HTTP_2)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .protocols(protocols)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_SCRIPT)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ServiceScriptApi::class.java)
    }

    companion object{
        var BASE_AUTH = "https://communicare-388309.et.r.appspot.com"
        var BASE_ML = "https://communicare-upload-3eilltznia-et.a.run.app/"
        var BASE_SCRIPT = "https://script-dot-communicare-388309.et.r.appspot.com/"
    }
}