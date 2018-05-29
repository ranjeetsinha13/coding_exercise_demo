package com.rs.mylivn.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rs.mylivn.BuildConfig.BASE_URL
import com.rs.mylivn.api.MockClient
import com.rs.mylivn.api.RemoteImageApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule(private val remoteImageApi: RemoteImageApi) {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
            OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()


    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                    // This will be changed when data will come from real APIs
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build()

    //@Provides
    //@Singleton
    //fun provideRemoteImageService(retrofit: Retrofit): RemoteImageApi =
    //        retrofit.create(RemoteImageApi::class.java)

    @Provides
    @Singleton
    fun provideRemoteImageService(): RemoteImageApi = remoteImageApi

}