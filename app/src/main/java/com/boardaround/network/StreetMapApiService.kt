package com.boardaround.network

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface StreetMapApiInterface {
    @GET("search")
    suspend fun search(
        @Query("q") query: String,
        @Query("format") format: String = "jsonv2",
        @Query("limit") limit: Int = 5,
        @Query("accept-language") language: String = "it",
        @Query("countrycodes") countryCodes: String? = null,
    ): List<StreetMapApiResponse>
}

data class StreetMapApiResponse(
    @SerializedName("display_name")
    val displayName: String?,
    val lat: String?,
    val lon: String?
)
