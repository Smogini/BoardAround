package com.boardaround.network

import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitInstance {
    val api: GameApiService by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val tikXml = TikXml.Builder()
            .exceptionOnUnreadXml(false)
            .build()

        Retrofit.Builder()
            .baseUrl("https://boardgamegeek.com/")
            .client(client)
            .addConverterFactory(TikXmlConverterFactory.create(tikXml))
            .build()
            .create(GameApiService::class.java)
    }
}