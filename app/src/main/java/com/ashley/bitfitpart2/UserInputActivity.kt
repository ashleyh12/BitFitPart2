package com.ashley.bitfitpart2

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ashley.bitfitpart2.data.Food
import com.ashley.bitfitpart2.db.FoodDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserInputActivity : AppCompatActivity(){

    @RequiresApi(Build.VERSION_CODES.N)
    fun createNewFood(food: String, calories: Int) : Food{
        return Food(food, calories, Calendar.getInstance().get(Calendar.DAY_OF_YEAR))
    }

    @OptIn(DelicateCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_input)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "BitFit-Part2"
            actionBar.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.purple_500)))
        }

        val getFood: EditText = findViewById(R.id.food_ET)
        val getCal: EditText = findViewById(R.id.cal_ET)
        val saveBTN: Button = findViewById(R.id.save_food)

        saveBTN.setOnClickListener {
            val foodName = getFood.text.toString()
            val calCount = getCal.text.toString()

            if(foodName == "" || calCount == ""){
                Toast.makeText(this@UserInputActivity, "Please enter food name and calories",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            GlobalScope.launch(Dispatchers.IO)  {
                val foodDao = FoodDatabase.getInstance(this@UserInputActivity)?.foodDao()
                foodDao?.insertFood(createNewFood(foodName, calCount.toInt()))
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}