package ru.milovidov.android.films2.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.milovidov.android.films2.entities.favoriteEntity


@Database(entities = [favoriteEntity::class], version = 1, exportSchema = false)
abstract class FavoriteDatabase : RoomDatabase() {

    companion object {
        private var instance: FavoriteDatabase? = null

        @Synchronized
        fun getInstance(context: Context): FavoriteDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    FavoriteDatabase::class.java,
                    "favorite_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build()
            }
            return instance!!
        }

        private val roomCallBack: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                //

            }
        }
    }

    abstract fun dao(): FavoriteDao




}