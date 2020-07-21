package ru.skillbranch.gameofthrones.repositories

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.remote.res.CharacterRes
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes
import ru.skillbranch.gameofthrones.data.remote.service.GoTApiService
import ru.skillbranch.gameofthrones.utils.ServiceLocator
import ru.skillbranch.gameofthrones.utils.getCharacterId

object GoTRepository {

    private val dao = ServiceLocator.provideDB().appDao()
    private val api = GoTApiService.create()
    private val scope = CoroutineScope(Job() + IO)

    suspend fun insertHouse(house: HouseRes) {
        withContext(IO) {
            dao.insertHouse(house.toHouse())
        }
    }

    suspend fun insertCharacters(characters: List<CharacterRes>) {
        Log.d("LOAD_DATA!!", "Insert into DB - characterRes: $characters")
        val charactersForDb = characters.map { it.toCharacter() }
        withContext(IO) {
            Log.d("LOAD_DATA!!", "Insert into DB - characterForDB:$charactersForDb")
            dao.insertCharacters(charactersForDb)
        }
    }

    suspend fun findCharacterFullById(id: String): CharacterFull {
        return withContext(IO) {
            val character = dao.getCharacterById(id)!!
            val mother = dao.getRelativeCharacter(character.mother.getCharacterId())
            val father = dao.getRelativeCharacter(character.father.getCharacterId())
            character.toCharacterFull(mother, father)
        }
    }

    suspend fun findCharactersByHouseName(name: String): List<CharacterItem> {
        return withContext(IO) {
            dao.getCharactersByHouseName(name).map { it.toCharacterItem() }
        }
    }

    fun findCharactersByHouse(name: String) = dao.getCharactersItem(name)

    suspend fun getNeedHouseWithCharacters(
        houseNames: Array<String>
    ): List<Pair<HouseRes, List<CharacterRes>>> {
        val needHouses = ArrayList<Pair<HouseRes, List<CharacterRes>>>()
            for (name in houseNames) {
                scope.launch {
                    val house = api.fetchHouseByName(name).first()
                    val characters = mutableListOf<CharacterRes>()
                    house.swornMembers.forEach { character ->
                        Log.d("LOAD_DATA!!", "$character")
                        scope.launch {
                            api.fetchCharacterById(character.getCharacterId())
                                .apply {
                                    houseId = house.houseId
                                    words = house.words
                                }.also { characters.add(it) }
                        }
                    }
                    needHouses.add(house to characters)
                }.join()
            }
        return needHouses
    }

//    suspend fun isNeedUpdate() = dao.getCharacters().isNullOrEmpty() && dao.getHouses().isNullOrEmpty()
    suspend fun isNeedUpdate() = true
}
