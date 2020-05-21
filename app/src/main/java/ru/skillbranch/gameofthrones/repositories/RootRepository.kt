package ru.skillbranch.gameofthrones.repositories

import android.util.Log
import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.local.entities.RelativeCharacter
import ru.skillbranch.gameofthrones.data.remote.res.CharacterRes
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes
import ru.skillbranch.gameofthrones.data.remote.service.GoTApiService
import ru.skillbranch.gameofthrones.utils.ServiceLocator
import ru.skillbranch.gameofthrones.utils.getCharacterId
import kotlin.coroutines.CoroutineContext

object RootRepository : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = IO + Job()

    private val apiService = GoTApiService.create()
    private val scope = CoroutineScope(IO + Job())
    private val dao = ServiceLocator.provideDB().appDao()

    private const val MAX_HOUSE_PAGE = 9 //when pageSize = 50

    /**
     * Получение данных о всех домах
     * @param result - колбек содержащий в себе список данных о домах
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getAllHouses(result: (houses: List<HouseRes>) -> Unit) {
        val allHouses = mutableListOf<HouseRes>()
        launch {
            try {
                launch {
                    for (page in 1..MAX_HOUSE_PAGE) {
                        launch {
                            allHouses.addAll(apiService.fetchAllHouses(page))
                        }
                    }
                }.join()
                result(allHouses)
            } catch (e: Exception) {
                Log.d("Repository", "$e")
            }
        }
    }

    /**
     * Получение данных о требуемых домах по их полным именам (например фильтрация всех домов)
     * @param houseNames - массив полных названий домов (смотри AppConfig)
     * @param result - колбек содержащий в себе список данных о домах
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getNeedHouses(vararg houseNames: String, result: (houses: List<HouseRes>) -> Unit) {
        val needHouses = mutableListOf<HouseRes>()
        launch {
            try {
                for (name in houseNames) {
                    needHouses.add(apiService.fetchHouseByName(name).first())
                }
                result(needHouses)
            } catch (e: Exception) {
                Log.d("Repository", "$e")
            }
        }
    }

    /**
     * Получение данных о требуемых домах по их полным именам и персонажах в каждом из домов
     * @param houseNames - массив полных названий домов (смотри AppConfig)
     * @param result - колбек содержащий в себе список данных о доме и персонажей в нем (Дом - Список Персонажей в нем)
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getNeedHouseWithCharacters(
        vararg houseNames: String,
        result: (houses: List<Pair<HouseRes, List<CharacterRes>>>) -> Unit
    ) {
        val needHouses = ArrayList<Pair<HouseRes, List<CharacterRes>>>()
        launch {
            for (name in houseNames) {
                val house = apiService.fetchHouseByName(name).first()
                val characters = mutableListOf<CharacterRes>()
                launch {
                    house.swornMembers.forEach { characterId ->
                        launch {
                            apiService.fetchCharacterById(characterId.substringAfterLast("/"))
                                .apply {
                                    houseId = house.houseId
                                    words = house.words
                                }.also { characters.add(it) }
                        }
                    }
                }.join()
                needHouses.add(house to characters)
            }
            result(needHouses)
        }
    }

    /**
     * Запись данных о домах в DB
     * @param houses - Список домов (модель HouseRes - модель ответа из сети)
     * необходимо произвести трансформацию данных
     * @param complete - колбек о завершении вставки записей db
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun insertHouses(houses: List<HouseRes>, complete: () -> Unit) {
        val housesForDb = houses.map { it.toHouse() }
        launch {
            dao.insertHouses(housesForDb)
            complete()
        }
    }

    /**
     * Запись данных о пересонажах в DB
     * @param characters - Список персонажей (модель CharterRes - модель ответа из сети)
     * необходимо произвести трансформацию данных
     * @param complete - колбек о завершении вставки записей db
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun insertCharacters(characters: List<CharacterRes>, complete: () -> Unit) {
        val charactersForDb = characters.map { it.toCharacter() }
        launch {
            dao.insertCharacters(charactersForDb)
            complete()
        }
    }

    /**
     * При вызове данного метода необходимо выполнить удаление всех записей в db
     * @param complete - колбек о завершении очистки db
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun dropDb(complete: () -> Unit) {
        launch {
            dao.dropDb()
            complete()
        }
    }

    /**
     * Поиск всех персонажей по имени дома, должен вернуть список краткой информации о персонажах
     * дома - смотри модель CharterItem
     * @param name - краткое имя дома (его первычный ключ)
     * @param result - колбек содержащий в себе список краткой информации о персонажах дома
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun findCharactersByHouseName(name: String, result: (characters: List<CharacterItem>) -> Unit) {
        launch {
            result(dao.getCharactersByHouseName(name).map { it.toCharacterItem() })
        }
    }

    /**
     * Поиск персонажа по его идентификатору, должен вернуть полную информацию о персонаже
     * и его родственных отношения - смотри модель CharterFull
     * @param id - идентификатор персонажа
     * @param result - колбек содержащий в себе полную информацию о персонаже
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun findCharacterFullById(id: String, result: (character: CharacterFull) -> Unit) {
        launch {
            val character = dao.getCharacterById(id)
            var mother: RelativeCharacter? = null
            var father: RelativeCharacter? = null
            if (character!!.mother.isNotEmpty()) {
                mother =
                    dao.getCharacterById(character.mother.getCharacterId())?.toRelativeCharacter()
            }
            if (character.father.isNotEmpty()) {
                father =
                    dao.getCharacterById(character.father.getCharacterId())?.toRelativeCharacter()
            }

            result(character.toCharacterFull(mother, father))
        }
    }

    /**
     * Метод возвращет true если в базе нет ни одной записи, иначе false
     * @param result - колбек о завершении очистки db
     */
    fun isNeedUpdate(result: (isNeed: Boolean) -> Unit) {
        launch {
            val isNeed = dao.getCharacters().isNullOrEmpty() && dao.getHouses().isNullOrEmpty()
            result(isNeed)
        }
    }

}