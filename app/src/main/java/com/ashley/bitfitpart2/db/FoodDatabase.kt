package com.ashley.bitfitpart2.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ashley.bitfitpart2.data.Food

@Database(entities = [Food::class], version = 1)
abstract class FoodDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao

    companion object {
        private val LOG_TAG = FoodDatabase::class.java.simpleName
        private val LOCK = Any()
        private const val DATABASE_NAME = "food_list"
        private var sInstance: FoodDatabase? = null
        fun getInstance(context: Context): FoodDatabase? {
            if (sInstance == null) {
                synchronized(LOCK) {
                    Log.d(LOG_TAG, "Creating new database instance")
                    sInstance = Room.databaseBuilder(context.applicationContext,
                            FoodDatabase::class.java, DATABASE_NAME)
                            .build()
                }
            }
            Log.d(LOG_TAG, "Getting the database instance")
            return sInstance
        }
    }
}