package com.ashley.bitfitpart2.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ashley.bitfitpart2.data.Food

@Dao
interface FoodDao {
    @Query("SELECT * FROM Food")
    fun getAll(): List<Food>

    @Query("SELECT * FROM Food WHERE food_name LIKE :food AND " +
            "calorie_count LIKE :calorie LIMIT 1")
    fun findByName(food: String, calorie: Int): Food

    @Insert
    fun insertAll(vararg foods: Food)

    @Insert
    fun insertFood(food: Food)

    @Delete
    fun delete(food: Food)
}