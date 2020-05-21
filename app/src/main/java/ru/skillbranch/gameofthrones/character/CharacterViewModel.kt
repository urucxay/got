package ru.skillbranch.gameofthrones.character

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import ru.skillbranch.gameofthrones.repositories.GoTRepository
import ru.skillbranch.gameofthrones.utils.Event

class CharacterViewModel(private val id: String) : ViewModel() {

    val character = liveData { emit(GoTRepository.findCharacterFullById(id)) }

    private val _openParentEvent = MutableLiveData<Event<String>>()
    val openParentEvent: LiveData<Event<String>> = _openParentEvent

    fun openParent(id: String) {
        _openParentEvent.value = Event(id)
    }

}