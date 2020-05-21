package ru.skillbranch.gameofthrones.utils

import android.content.Context
import ru.skillbranch.gameofthrones.data.local.db.AppDatabase

object ServiceLocator {

    private lateinit var appDatabase: AppDatabase

    fun init(context: Context) {
        appDatabase = AppDatabase.getInstance(context)
    }

    fun provideDB(): AppDatabase = appDatabase
}