package com.boardaround.utils

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "item")
data class Game(
    @Attribute(name = "id")
    val id: Int,

    @PropertyElement(name = "name")
    val name: String,

    @Path("thumbnail")
    @PropertyElement(name = "value")
    val imageUrl: String? = null,

    @Path("description")
    @PropertyElement(name = "value")
    val description: String? = null,

    @Attribute(name = "yearpublished")
    val yearPublished: Int? = null
)

@Xml(name = "items")
data class GameSearchResult(
    @Attribute(name = "total")
    val total: Int, // Aggiungi questa proprietà

    @Element(name = "item")
    val games: List<Game> = emptyList()
)