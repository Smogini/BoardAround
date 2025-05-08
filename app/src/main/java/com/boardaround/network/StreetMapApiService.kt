package com.boardaround.network


import com.google.gson.annotations.SerializedName
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface StreetMapApiInterface {
    @GET("search")
    fun search(
        @Query("q") query: String,
        @Query("format") format: String = "jsonv2",
        @Query("limit") limit: Int = 5,
        @Query("accept-language") language: String = "it",
        @Query("countrycodes") countryCodes: String? = null,
    ): Call<List<StreetMapApiResponse>>

    @GET("reverse")
    fun reverse(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("format") format: String = "jsonv2",
        @Query("accept-language") language: String = "it"
    ): Call<StreetMapApiResponse>
}

data class StreetMapApiResponse(
    @SerializedName("display_name")
    val displayName: String?,
    val lat: String?,
    val lon: String?
)

object NominatimClient {
    private const val BASE_URL = "https://nominatim.openstreetmap.org/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Logga il corpo della richiesta e risposta
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Aggiungi l'interceptor
        .build()

    val instance: StreetMapApiInterface by lazy {
        val client = okhttp3.OkHttpClient.Builder()
            .addInterceptor(UserAgentInterceptor())
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StreetMapApiInterface::class.java)
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
}