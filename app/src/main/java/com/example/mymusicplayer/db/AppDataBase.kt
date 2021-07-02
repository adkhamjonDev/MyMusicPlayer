package com.example.mymusicplayer.db
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mymusicplayer.models.MusicClass
import com.example.mymusicplayer.models.MusicClassRoom

@Database(entities = [MusicClassRoom::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun musicDao(): MusicDao
    companion object {
        private var appDatabase: AppDataBase? = null
        @Synchronized
        fun getInstance(context: Context): AppDataBase {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(context, AppDataBase::class.java, "music_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return appDatabase!!
        }
    }
}