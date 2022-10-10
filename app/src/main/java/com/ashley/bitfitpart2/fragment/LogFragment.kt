package com.ashley.bitfitpart2.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ashley.bitfitpart2.R
import com.ashley.bitfitpart2.RecyclerViewAdapter
import com.ashley.bitfitpart2.data.Food
import com.ashley.bitfitpart2.db.FoodDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class LogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.log_fragment, container, false)
    }

    @OptIn(DelicateCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var listData = ArrayList<Food>()
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        Log.i("DATESTUFF", dayOfYear.toString())

        val rView = view.findViewById<RecyclerView>(R.id.recyclerView)
        rView.layoutManager = LinearLayoutManager(view.context)


        GlobalScope.launch(Dispatchers.IO)  {
            val foodDao = FoodDatabase.getInstance(view.context)?.foodDao()
            if (foodDao != null) {
                listData = foodDao.getAll() as ArrayList<Food>
            }

            val adapter = RecyclerViewAdapter(listData)

            launch(Dispatchers.Main) {
                rView.adapter = adapter
            }
        }

        }
    }