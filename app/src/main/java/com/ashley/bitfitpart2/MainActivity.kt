package com.ashley.bitfitpart2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ashley.bitfitpart2.data.Food
import com.ashley.bitfitpart2.db.FoodDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ashley.bitfitpart2.fragment.LogFragment
import com.ashley.bitfitpart2.fragment.SummaryFragment
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var bottomNav:BottomNavigationView
    private val channelId = "com.ashley.bitfitpart2"
    private val description = "New alert to add your food for the day."



    @RequiresApi(Build.VERSION_CODES.N)
    @OptIn(DelicateCoroutinesApi::class)
    override fun onStart() {
        super.onStart()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.getItemId()

        if (id == R.id.clear_data) {
            clearData()
            Toast.makeText(this, "Clear All Food Data ", Toast.LENGTH_LONG).show()
            return true
        }

        return super.onOptionsItemSelected(item)

    }

    @OptIn(DelicateCoroutinesApi::class)
    fun clearData(){
        GlobalScope.launch(Dispatchers.IO)  {
            val foodDao = FoodDatabase.getInstance(this@MainActivity)?.foodDao()
            var listData = ArrayList<Food>()
            if (foodDao != null) {
                listData = foodDao.getAll() as ArrayList<Food>
            }

            if (listData.size > 0) {
                for(data in listData){
                    foodDao?.delete(data)
                }
            }
        }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "BitFit-Part2"
            actionBar.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.purple_500)))
        }

        val addFoodBtn :Button = findViewById(R.id.add_food_btn)

        addFoodBtn.setOnClickListener{
            val intent = Intent(this, UserInputActivity::class.java)
            startActivity(intent)
        }

        // bottom nav
        loadFragment(LogFragment())
        bottomNav = findViewById<View>(R.id.bottomNav) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.log -> {
                    loadFragment(LogFragment())
                    true
                }
                R.id.summary -> {
                    loadFragment(SummaryFragment())
                    true
                }
                else -> false
            }
        }

        createNotificationChannel()
        var builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Food Alert!")
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)


        var notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notifyThread = Thread (Runnable {
            while (true) {
                notificationManager.notify(1234, builder.build())
                Thread.sleep(86400 * 24)
            }
        })
        notifyThread.start()

        //notificationManager.notify(1234, builder.build())
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}