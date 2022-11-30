package com.example.imagegeneratordemo.di

import com.example.imagegeneratordemo.data.remote.OpenAiService
import com.example.imagegeneratordemo.utils.ApiConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    companion object {
        const val CONNECT_TIMEOUT_IN_MS = 30000
        val LOGGING_LEVEL = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    fun provideBaseUrl() = ApiConstants.BASE_URL

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = LOGGING_LEVEL
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            request = request.newBuilder()
                .header(ApiConstants.KEY_HEADER_CONTENT_TYPE, ApiConstants.VALUE_HEADER_JSON)
                .header(ApiConstants.KEY_HEADER_AUTHORIZATION, ApiConstants.VALUE_HEADER_BEARER)
                .build()
            chain.proceed(request)
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        interceptor: Interceptor
    ) = OkHttpClient.Builder()
        .connectTimeout(CONNECT_TIMEOUT_IN_MS.toLong(), TimeUnit.MILLISECONDS)
        .addInterceptor(loggingInterceptor)
        .addNetworkInterceptor(interceptor)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideOpenAiService(retrofit: Retrofit): OpenAiService =
        retrofit.create(OpenAiService::class.java)

    @Provides
    @Singleton
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}