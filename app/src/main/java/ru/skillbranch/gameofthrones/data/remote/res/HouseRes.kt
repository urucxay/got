package ru.skillbranch.gameofthrones.data.remote.res

import ru.skillbranch.gameofthrones.data.local.entities.House

data class HouseRes(
    val url: String,
    val name: String,
    val region: String,
    val coatOfArms: String,
    val words: String,
    val titles: List<String> = listOf(),
    val seats: List<String> = listOf(),
    val currentLord: String,
    val heir: String,
    val overlord: String,
    val founded: String,
    val founder: String,
    val diedOut: String,
    val ancestralWeapons: List<String> = listOf(),
    val cadetBranches: List<String> = listOf(),
    val swornMembers: List<String> = listOf()
) {

    val houseId: String = when {
        name.contains("Stark") -> "Stark"
        name.contains("Lannister") -> "Lannister"
        name.contains("Targaryen") -> "Targaryen"
        name.contains("Baratheon") -> "Baratheon"
        name.contains("Greyjoy") -> "Greyjoy"
        name.contains("Martell") -> "Martell"
        name.contains("Tyrell") -> "Tyrell"
        else -> "Unknown"
    }

    fun toHouse(): House = House(
        id = this.houseId,
        name = this.name,
        coatOfArms = this.coatOfArms,
        words = this.words,
        region = this.region,
        titles = this.titles,
        seats = this.seats,
        currentLord = this.currentLord,
        heir = this.heir,
        overlord = this.overlord,
        founded = this.founded,
        founder = this.founder,
        diedOut = this.diedOut,
        ancestralWeapons = this.ancestralWeapons
    )

}