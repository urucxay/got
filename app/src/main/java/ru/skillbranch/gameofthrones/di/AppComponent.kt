package ru.skillbranch.gameofthrones.di

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.skillbranch.gameofthrones.house.HouseFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(app: Application)
    fun inject(newsFragment: HouseFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }
}

