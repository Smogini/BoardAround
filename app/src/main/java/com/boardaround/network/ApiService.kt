package com.boardaround.network

import com.boardaround.data.entities.GameDetailsResult
import com.boardaround.data.entities.GameSearchResult
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private fun <T> createApi(baseUrl: String, serviceInterface: Class<T>): T {
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)

    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val tikXml = TikXml.Builder()
        .exceptionOnUnreadXml(false)
        .build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(TikXmlConverterFactory.create(tikXml))
        .build()
        .create(serviceInterface)
}

object ApiService {
    val gameApi: GameApiInterface by lazy {
        createApi("https://boardgamegeek.com/", GameApiInterface::class.java)
    }

    val triviaApi: TriviaApiService by lazy {
        createApi("https://opentdb.com/", TriviaApiService::class.java)
    }
}

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
