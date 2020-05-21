package ru.skillbranch.gameofthrones.di

import android.content.Context
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ru.skillbranch.gameofthrones.data.local.db.AppDatabase
import ru.skillbranch.gameofthrones.data.remote.service.GoTApiService
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideDb(context: Context) = AppDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideService() = GoTApiService.create()

    @Provides
    @Singleton
    fun provideCoroutineScopeIO() = CoroutineScope(Dispatchers.IO)

}