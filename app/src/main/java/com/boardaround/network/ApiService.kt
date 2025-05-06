package com.boardaround.network

import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private fun <T> createApi(baseUrl: String, serviceInterface: Class<T>, xmlResponse: Boolean): T {
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)

    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val builder = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)

    if (xmlResponse) {
        val tikXml = TikXml.Builder()
            .exceptionOnUnreadXml(false)
            .build()
        builder.addConverterFactory(TikXmlConverterFactory.create(tikXml))
    } else {
        builder.addConverterFactory(GsonConverterFactory.create())
    }

    return builder.build().create(serviceInterface)
}

object ApiService {
    val gameApi: GameApiInterface by lazy {
        createApi("https://boardgamegeek.com/", GameApiInterface::class.java, true)
    }

    val triviaApi: TriviaApiInterface by lazy {
        createApi("https://opentdb.com/", TriviaApiInterface::class.java, false)
    }
}
