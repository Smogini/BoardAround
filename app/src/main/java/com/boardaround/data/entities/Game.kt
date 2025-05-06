package com.boardaround.data.entities

import androidx.core.text.HtmlCompat
import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml

data class Game(
    val id: Int,
    val name: String,
    val description: String? = "No description",
    val imageUrl: String? = "No image",
    val publisher: String? = "No publisher",
    val yearPublished: Int? = -1,
    val minPlayers: Int? = -1,
    val maxPlayers: Int? = -1,
    val playingTime: Int? = -1,
    val expansions: List<BoardGameExpansion>? = emptyList()
)

/*
* Class that defines the structure of the xml returned by the GameApiInterface.searchGames
*/
@Xml(name = "items")
data class GameSearchResult(
    @Attribute(name = "total")
    val total: Int,

    @Element(name = "item")
    val games: List<SearchGame>? = emptyList()
)

@Xml(name = "item")
data class SearchGame(
    @Attribute(name = "id")
    val id: Int,

    @Element(name = "name")
    val name: SearchNameElement,
)

@Xml(name = "name")
data class SearchNameElement(
    @Attribute(name = "value")
    val value: String
)

/*
* Class that defines the structure of the xml returned by the GameApiInterface.gameInfo
*/
@Xml(name = "boardgames")
data class GameDetailsResult(
    @Element(name = "boardgame")
    val game: GameDetails
)

@Xml(name = "boardgame")
data class GameDetails(
    @Attribute(name = "objectid")
    val id: Int,

    @Element(name = "name")
    val names: List<DetailNameElement>? = emptyList(),

    @PropertyElement(name = "description")
    val description: String? = "No description",

    @PropertyElement(name = "minplayers")
    val minPlayers: Int? = -1,

    @PropertyElement(name = "maxplayers")
    val maxPlayers: Int? = -1,

    @PropertyElement(name = "playingtime")
    val playingTime: Int? = -1,

    @PropertyElement(name = "image")
    val imageUrl: String? = "No image",

    @Element(name = "yearpublished")
    val yearPublished: YearPublished? = null,

    @Element(name = "boardgamepublisher")
    val publisher: List<GamePublisher>? = emptyList(),

    @Element(name = "boardgameexpansion")
    val expansions: List<BoardGameExpansion>? = emptyList()
)

@Xml(name = "boardgamepublisher")
data class GamePublisher(
    @Attribute(name = "objectid")
    val id: Int,

    @TextContent
    val name: String
)

@Xml(name = "boardgameexpansion")
data class BoardGameExpansion(
    @Attribute(name = "objectid")
    val id: Int,

    @TextContent
    val title: String
)

@Xml(name = "name")
data class DetailNameElement(
    @Attribute(name = "primary")
    val primary: Boolean? = false,

    @TextContent
    val value: String
)

@Xml(name = "yearpublished")
data class YearPublished(
    @Attribute(name = "value")
    val value: Int
)

fun GameDetails.toGame(): Game = Game(
    id = id,
    name = decodeHtmlText(names?.firstOrNull()?.value ?: ""),
    description = decodeHtmlText(description ?: ""),
    imageUrl = imageUrl,
    yearPublished = yearPublished?.value,
    publisher = this.publisher?.get(0)?.name,
    minPlayers = this.minPlayers,
    maxPlayers = this.maxPlayers,
    playingTime = this.playingTime,
    expansions = this.expansions
)

private fun decodeHtmlText(toConvert: String): String {
    return HtmlCompat.fromHtml(toConvert, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    .toString()
                    .replace("<br/>", "\n")
                    .trim()
}