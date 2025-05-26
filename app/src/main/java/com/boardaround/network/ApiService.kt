package com.boardaround.network

import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private fun <T> createApi(baseUrl: String, serviceInterface: Class<T>, xmlResponse: Boolean = false): T {
    val client = OkHttpClient.Builder()
        .addInterceptor(UserAgentInterceptor())
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
        createApi("https://opentdb.com/", TriviaApiInterface::class.java)
    }

    val streetApi: StreetMapApiInterface by lazy {
        createApi("https://nominatim.openstreetmap.org/", StreetMapApiInterface::class.java)
    }

    val newsApi: NewsApiInterface by lazy {
        createApi("https://newsapi.org/", NewsApiInterface::class.java)
    }

}

class UserAgentInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestWithUserAgent = originalRequest.newBuilder()
            .header(
                "User-Agent",
                "BoardAroundApp/1.0"
            )
            .build()
        return chain.proceed(requestWithUserAgent)
    }
}
