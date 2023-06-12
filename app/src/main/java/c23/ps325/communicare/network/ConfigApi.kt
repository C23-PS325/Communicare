package c23.ps325.communicare.network

import android.content.Context
import androidx.room.Room
import c23.ps325.communicare.BuildConfig
import c23.ps325.communicare.database.CommunicareDAO
import c23.ps325.communicare.database.CommunicareDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120,TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .protocols(protocols)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_ML)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ServiceMLApi::class.java)
    }

    companion object{
        var BASE_AUTH = "https://communicare-388309.et.r.appspot.com"
        var BASE_ML = "https://communicare-upload-3eilltznia-et.a.run.app/"
    }
    @Provides
    fun provideDAO(db: CommunicareDB): CommunicareDAO = db.communicareDAO()

    @Singleton
    @Provides
    fun provideDB(@ApplicationContext context: Context): CommunicareDB {
        return Room.databaseBuilder(
            context.applicationContext,
            CommunicareDB::class.java,
            "communicare.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}