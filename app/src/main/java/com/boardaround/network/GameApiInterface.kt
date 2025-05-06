package com.boardaround.network

import com.boardaround.data.entities.GameDetailsResult
import com.boardaround.data.entities.GameSearchResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GameApiInterface {
    @GET("xmlapi2/search")
    suspend fun searchGames(
        @Query("query") query: String,
        @Query("type") type: String = "boardgame"
    ): GameSearchResult

    /*  Use the path parameters for the search on the game board.
    *   This alternative was used for how the API and their search URLs were created.
    *   (More info in: https://boardgamegeek.com/thread/99401/boardgamegeek-xml-api).
    *   Also, to work it needs the network_security_config.xml file
    */
    @GET("xmlapi/game/{id}")
    suspend fun getGameInfo(
        @Path("id") gameID: Int,
    ): GameDetailsResult
}