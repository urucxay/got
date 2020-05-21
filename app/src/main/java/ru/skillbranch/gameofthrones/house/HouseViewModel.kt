package ru.skillbranch.gameofthrones.house

import androidx.lifecycle.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.skillbranch.gameofthrones.data.local.entities.Character
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.repositories.GoTRepository
import ru.skillbranch.gameofthrones.utils.Event

class HouseViewModel(houseName: String) : ViewModel() {

    val members = GoTRepository.findCharactersByHouse(houseName)

    private val _openCharacterEvent = MutableLiveData<Event<String>>()
    val openCharacterEvent: LiveData<Event<String>> = _openCharacterEvent

    fun openCharacter(id: String) {
        _openCharacterEvent.value = Event(id)
    }


}