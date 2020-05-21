package ru.skillbranch.gameofthrones.data.remote.res

import ru.skillbranch.gameofthrones.data.local.entities.Character
import ru.skillbranch.gameofthrones.utils.getCharacterId

data class CharacterRes(
    val url: String,
    val name: String,
    val gender: String,
    val culture: String,
    val born: String,
    val died: String,
    val titles: List<String> = listOf(),
    val aliases: List<String> = listOf(),
    val father: String,
    val mother: String,
    val spouse: String,
    val allegiances: List<String> = listOf(),
    val books: List<String> = listOf(),
    val povBooks: List<String> = listOf(),
    val tvSeries: List<String> = listOf(),
    val playedBy: List<String> = listOf()
) {
    var houseId = ""
    var words = ""

    fun toCharacter() : Character = Character(
        id = this.url.getCharacterId(),
        name = this.name,
        gender = this.gender,
        culture = this.culture,
        born = this.born,
        died = this.died,
        titles = this.titles,
        aliases = this.aliases,
        mother = this.mother,
        father = this.father,
        spouse = this.spouse,
        houseId = this.houseId
    ).apply { words = this@CharacterRes.words }
}