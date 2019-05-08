package com.example.parkingipwr

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StatisticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        findViewById<Button>(R.id.button).setOnClickListener {
            ParkingiPwrRepository.getParkingPwrStats()
        }
    }


}
