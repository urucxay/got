package ru.skillbranch.gameofthrones.house

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HouseViewModelFactory constructor(
    private val house: String
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            if (isAssignableFrom(HouseViewModel::class.java)) {
                HouseViewModel(house)
            } else
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        } as T
}
