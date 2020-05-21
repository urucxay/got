package ru.skillbranch.gameofthrones.main

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.skillbranch.gameofthrones.AppConfig
import ru.skillbranch.gameofthrones.main.Status.*
import ru.skillbranch.gameofthrones.repositories.GoTRepository
import ru.skillbranch.gameofthrones.utils.Event
import ru.skillbranch.gameofthrones.utils.isOnline

sealed class Status {
    object LOADING : Status()
    object SUCCESS : Status()
    object ERROR : Status()
}

class MainViewModel : ViewModel() {

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status> = _status

    private val _showSnackBarEvent = MutableLiveData<Event<Unit>>()
    val showSnackBarEvent: LiveData<Event<Unit>> = _showSnackBarEvent

    fun checkData() {
        viewModelScope.launch {
            _status.value = LOADING
            if (GoTRepository.isNeedUpdate()) {
                if (isOnline()) {
                    val houses = GoTRepository.getNeedHouseWithCharacters(AppConfig.NEED_HOUSES)
                    houses.forEach {
                        Log.d("LOAD_DATA!!", "checkData - house:${it.first}, characters:${it.second}")
                        GoTRepository.insertHouse(it.first)
                        GoTRepository.insertCharacters(it.second)
                    }
                    _status.value = SUCCESS
                } else {
                    _status.value = ERROR
                }
            } else {
                _status.value = SUCCESS
            }
        }
    }

    fun showSnackBar() {
        _showSnackBarEvent.value = Event(Unit)
    }

}