package com.ashley.bitfitpart2.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Food(
        @ColumnInfo(name = "food_name") val foodName: String,
        @ColumnInfo(name = "calorie_count") val calorieCount: Int,
        @ColumnInfo(name = "day_of_year") val dayOfYear: Int,
        @PrimaryKey(autoGenerate = true) var id: Int = 0//last so that we don't have to pass an ID value or named arguments
)
