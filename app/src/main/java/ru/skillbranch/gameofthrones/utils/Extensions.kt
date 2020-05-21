package ru.skillbranch.gameofthrones.utils

fun String.getCharacterId() : String = this.substringAfterLast("/")