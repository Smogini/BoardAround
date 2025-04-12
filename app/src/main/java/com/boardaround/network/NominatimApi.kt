package com.boardaround.network

import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NominatimApi {
    @GET("search")
    fun search(
        @Query("q") query: String,
        @Query("format") format: String = "jsonv2",
        @Query("limit") limit: Int = 5,
        @Query("accept-language") language: String = "it",
    ): Call<List<SearchResult>>
}

data class SearchResult(
    val displayName: String,
    val lat: String,
    val lon: String
)

object NominatimClient {
    private const val BASE_URL = "https://nominatim.openstreetmap.org/"

    val instance: NominatimApi by lazy {
        val client = okhttp3.OkHttpClient.Builder()
            .addInterceptor(UserAgentInterceptor())
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // Imposta il client HTTP personalizzato
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NominatimApi::class.java)
    }

    class UserAgentInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val requestWithUserAgent = originalRequest.newBuilder()
                .header(
                    "User-Agent",
                    "BoardAroundApp/1.0"
                ) // Sostituisci con il nome della tua app e una versione
                .build()
            return chain.proceed(requestWithUserAgent)
        }
    }
}