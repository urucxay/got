package ru.skillbranch.gameofthrones.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class Character(
    @PrimaryKey
    val id: String,
    val name: String,
    val gender: String,
    val culture: String,
    val born: String,
    val died: String,
    val titles: List<String> = listOf(),
    val aliases: List<String> = listOf(),
    val father: String, //rel
    val mother: String, //rel
    val spouse: String,
    val houseId: String//rel
) {

    var words = ""

    fun toCharacterItem() = CharacterItem(
        id = this.id,
        house = this.houseId,
        name = this.name,
        titles = this.titles,
        aliases = this.aliases
    )

    fun toRelativeCharacter() = RelativeCharacter(
        id = this.id,
        name = this.name,
        house = this.houseId
    )

    fun toCharacterFull(
        mother: RelativeCharacter?,
        father: RelativeCharacter?
    ) = CharacterFull(
            id = this.id,
            name = this.name,
            words = this.words,
            born = this.born,
            died = this.died,
            titles = this.titles,
            aliases = this.aliases,
            house = this.houseId,
            father = father,
            mother = mother
    )
}

data class CharacterItem(
    val id: String,
    val house: String, //rel
    val name: String,
    val titles: List<String>,
    val aliases: List<String>
)

data class CharacterFull(
    val id: String,
    val name: String,
    val words: String,
    val born: String,
    val died: String,
    val titles: List<String>,
    val aliases: List<String>,
    val house:String, //rel
    val father: RelativeCharacter?,
    val mother: RelativeCharacter?
)

data class RelativeCharacter(
    val id: String,
    val name: String,
    val house:String //rel
)