package com.boardaround.data.entities

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "items")
data class GameSearchResult(
    @Attribute(name = "total")
    val total: Int,

    @Element(name = "item")
    val games: List<Game>? = emptyList(),

    @Attribute(name = "termsofuse")
    val termsofuse: String? = ""
)

@Xml(name = "item")
data class Game(
    @Attribute(name = "id")
    val id: Int,

    @Element(name = "name")
    val nameElement: NameElement,

    @Path("thumbnail")
    @PropertyElement(name = "value")
    val imageUrl: String? = "No image",

    @Path("description")
    @PropertyElement(name = "value")
    val description: String? = "No description",

    @Element(name = "yearpublished")
    val yearPublished: YearPublished?
)

@Xml(name = "name")
data class NameElement(
    @Attribute(name = "value")
    val value: String
)

@Xml(name = "yearpublished")
data class YearPublished(
    @Attribute(name = "value")
    val value: Int
)
