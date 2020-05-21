package ru.skillbranch.gameofthrones.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.skillbranch.gameofthrones.data.local.entities.Character
import ru.skillbranch.gameofthrones.data.local.entities.House

@Database(entities = [Character::class, House::class], version = 1, exportSchema = false)
@TypeConverters(StringTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao() : AppDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = synchronized(this) {
            instance ?: Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "got_database"
            )
                .fallbackToDestructiveMigration()
                .build().also { instance = it }
        }
    }
}