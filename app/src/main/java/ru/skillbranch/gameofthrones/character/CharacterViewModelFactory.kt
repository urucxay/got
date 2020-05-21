package ru.skillbranch.gameofthrones.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.skillbranch.gameofthrones.house.HouseViewModel

class CharacterViewModelFactory constructor(
    private val id: String
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            if (isAssignableFrom(CharacterViewModel::class.java)) {
                CharacterViewModel(id)
            } else
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        } as T
}
