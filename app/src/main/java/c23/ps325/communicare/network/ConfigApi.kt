package c23.ps325.communicare.network

import android.content.Context
import androidx.room.Room
import c23.ps325.communicare.database.CommunicareDAO
import c23.ps325.communicare.database.CommunicareDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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