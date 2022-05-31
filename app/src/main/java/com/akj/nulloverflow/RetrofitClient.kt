package com.akj.nulloverflow

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit? {

        /*
        val logClient = OkHttpClient.Builder()

        //로깅 인터셉터
        val loggingInterceptor = HttpLoggingInterceptor(object: HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.i("logging", "RetrofitClient - log() called / message: $message")
            }
        })

        //로깅인터셉터 등록
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        logClient.addInterceptor(loggingInterceptor)
         */
        if(retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(logClient.build())  //해당 레트로핏에 로깅인터셉터 단 OkHttpClient붙여줌
                .build()
        }

        return retrofit
    }
}