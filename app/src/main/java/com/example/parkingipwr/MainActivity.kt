package com.example.parkingipwr

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
