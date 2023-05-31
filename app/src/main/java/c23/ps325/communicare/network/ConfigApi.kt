package c23.ps325.communicare.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConfigApi {

    private const val BASE_URL = "https://communicare-388309.et.r.appspot.com"

    private  val logging : HttpLoggingInterceptor
        get(){
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            return httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        }

    private val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()

    @Singleton
    @Provides
    fun instance(): Retrofit =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()

    @Singleton
    @Provides
    fun endPoint(retrofit: Retrofit): ServiceApi =
        retrofit.create(ServiceApi::class.java)

}