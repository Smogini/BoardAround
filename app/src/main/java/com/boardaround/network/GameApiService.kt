package com.boardaround.network

import com.boardaround.utils.GameSearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface GameApiService {
    @GET("xmlapi2/search")
    suspend fun searchGames(
        @Query("query") query: String,
        @Query("type") type: String = "boardgame"
    ): GameSearchResult
}