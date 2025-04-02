package com.boardaround.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import retrofit2.Retrofit

object RetrofitInstance {
    val api: GameApiService by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY) // Logga il corpo della risposta

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val tikXml = TikXml.Builder()
            .exceptionOnUnreadXml(false) // Se vuoi ignorare elementi non mappati (opzionale)
            .build()

        Retrofit.Builder()
            .baseUrl("https://boardgamegeek.com/")
            .client(client) // Aggiungi l'OkHttpClient configurato
            .addConverterFactory(TikXmlConverterFactory.create(tikXml))
            .build()
            .create(GameApiService::class.java)
    }
}