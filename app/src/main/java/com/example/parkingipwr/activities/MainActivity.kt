package com.example.parkingipwr.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.parkingipwr.R
import com.example.parkingipwr.data.*

class MainActivity : AppCompatActivity(), IParkingChangeObserver {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ParkingiPwrRepository.attachObserver(this)

        ParkingiPwrRepository.getCurrentStats(object : IParkingResponseObserver{
            override fun notify(parkings: List<Place>){
                Log.e("hey", parkings.size.toString())
            }
        })

        //ParkingiPwrRepository.getLastStats()
    }

    override fun notifyChanged(changedParkings: List<Place>){
        Log.e("NOTIFY MAIN: ", changedParkings.size.toString())
    }

    fun displayCurrentState(view: View){

        val myintent = Intent(this, CurrentParkingsMapActivity::class.java)
        startActivity(myintent)
    }


    fun displayStatistics(view: View){
        val myintent = Intent(this, StatisticsActivity::class.java)
        startActivity(myintent)
    }


}