package com.lifendry.laundry.lifendry.service

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LifendryRetrofit {
    private val interceptor = Interceptor {
        val url = it.request().url().newBuilder()
            .build()
        val request = it.request()
            .newBuilder()
            .url(url)
            .build()

        it.proceed(request)
    }

    private val client = OkHttpClient().newBuilder().addInterceptor(interceptor).build()

    fun api(BASE_URL: String): ApiService {
        return Retrofit.Builder().client(client)
            .baseUrl("http://"+BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(ApiService::class.java)
    }

    val gson = GsonBuilder()
    .setDateFormat("yyyy-MM-dd HH:mm:ss")
    .create()
}