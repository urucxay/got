package ru.skillbranch.gameofthrones.house

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.skillbranch.gameofthrones.data.local.entities.Character
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.repositories.GoTRepository
import ru.skillbranch.gameofthrones.utils.Event

class HouseViewModel(houseName: String) : ViewModel() {

    private val members = GoTRepository.findCharactersByHouse(houseName)
    private val query = MutableLiveData<String>("")

    private val _openCharacterEvent = MutableLiveData<Event<String>>()
    val openCharacterEvent: LiveData<Event<String>> = _openCharacterEvent

    fun openCharacter(id: String) {
        _openCharacterEvent.value = Event(id)
    }

    fun handleSearch(query: String?) {
        Log.d("TEST_SEARCH", "query -> $query")
        this.query.value = query
        Log.d("TEST_SEARCH", "query.value -> ${this.query.value}")
    }

    val testMembers = Transformations.map(members) {list ->
        Log.d("TEST_SEARCH", "list -> $list")
        list.filter { character -> character.name.contains(query.value!!)
        }
    }

    fun getMembers(): LiveData<List<CharacterItem>> {
        val result = MediatorLiveData<List<CharacterItem>>()

        val filterFn = {
            val queryStr = query.value!!
            val members = members.value ?: mutableListOf()

            result.value = if (queryStr.isEmpty()) members else members.filter { it.name.contains(queryStr, true)}
        }

        result.addSource(members){ filterFn.invoke() }
        result.addSource(query){ filterFn.invoke() }

        return result
    }


}