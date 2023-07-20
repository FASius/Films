package com.example.films.di

import android.content.Context
import com.example.films.Constants
import com.example.films.R
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

private const val API_KEY = "API_KEY"
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, converterFactory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setDateFormat(Constants.DATA_FORMAT)
            .create()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun provideInterceptor(@Named(API_KEY) apiKey: String): Interceptor {
        return Interceptor {
            val original = it.request()

            val request = original.newBuilder()
                .header("X-API-KEY", apiKey)
                .build()

            return@Interceptor it.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named(API_KEY)
    fun provideApiKey(@ApplicationContext context: Context): String {
        return context.getString(R.string.api_key)
    }
}