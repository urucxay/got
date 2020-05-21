package ru.skillbranch.gameofthrones.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.skillbranch.gameofthrones.data.local.entities.Character
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.local.entities.House
import ru.skillbranch.gameofthrones.data.local.entities.RelativeCharacter

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHouse(house: House)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHouses(houses: List<House>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCharacters(characters: List<Character>)

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacterById(id: String): Character?

    @Query("SELECT * FROM characters WHERE houseId = :name ORDER BY name ASC")
    suspend fun getCharactersByHouseName(name: String): List<Character>

    @Query("SELECT id, houseId AS house, name, titles, aliases  FROM characters WHERE houseId = :name ORDER BY name ASC")
    fun getCharactersItem(name: String): LiveData<List<CharacterItem>>

    @Query("SELECT id, houseId AS house, name  FROM characters WHERE id = :id ORDER BY name ASC")
    suspend fun getRelativeCharacter(id: String): RelativeCharacter

    @Query("SELECT * FROM characters ORDER BY name ASC")
    suspend fun getCharacters(): List<Character>

    @Query("SELECT * FROM houses ORDER BY name ASC")
    suspend fun getHouses(): List<House>

    @Transaction
    suspend fun dropDb() {
        clearHouses()
        clearCharacters()
    }

    @Query("DELETE FROM characters")
    suspend fun clearCharacters()

    @Query("DELETE FROM houses")
    suspend fun clearHouses()

}