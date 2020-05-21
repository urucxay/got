package ru.skillbranch.gameofthrones.data.local.db

import androidx.room.TypeConverter

class StringTypeConverters {

    @TypeConverter
    fun stringToList(string: String) = string.split(";")

    @TypeConverter
    fun listToString(list: List<String>) = list.joinToString (";")

}