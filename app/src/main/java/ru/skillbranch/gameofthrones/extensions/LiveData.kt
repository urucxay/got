package ru.skillbranch.gameofthrones.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <R, A, B> LiveData<A>.combine(other: LiveData<B>, onChange: (A, B) -> R): MediatorLiveData<R> {

    var firstSourceEmitted = false
    var secondSourceEmitted = false

    val result = MediatorLiveData<R>()

    val mergeFn = {
        val firstSourceValue = this.value
        val secondSourceValue = other.value

        if (firstSourceEmitted && secondSourceEmitted) {
            result.value = onChange(firstSourceValue!!, secondSourceValue!!)
        }
    }
    result.addSource(this) { firstSourceEmitted = true; mergeFn() }
    result.addSource(other) { secondSourceEmitted = true; mergeFn() }

    return result
}