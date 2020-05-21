package ru.skillbranch.gameofthrones

import android.app.Application
import ru.skillbranch.gameofthrones.utils.ServiceLocator

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ServiceLocator.init(this)
    }

}