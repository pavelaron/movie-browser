package com.mbh.moviebrowser.repository

import com.mbh.moviebrowser.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiHelper {
    private const val readAccessId = BuildConfig.TMDB_READ_ACCESS_ID

    private val okHttpClient = OkHttpClient.Builder()
        .apply {
            addInterceptor(Interceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Authorization", "Bearer $readAccessId")
                    .header("Accept", "application/json")
                    .method(original.method, original.body)
                    .build()

                return@Interceptor chain.proceed(request)
            })
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
