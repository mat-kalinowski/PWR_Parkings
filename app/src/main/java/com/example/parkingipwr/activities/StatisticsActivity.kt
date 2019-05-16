package com.example.parkingipwr.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import android.graphics.Color
import android.widget.ListView
import com.example.parkingipwr.R
import com.example.parkingipwr.adapters.StatisticsAdapter
import com.example.parkingipwr.adapters.StatisticsObject
import com.example.parkingipwr.data.*
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData
import kotlinx.android.synthetic.main.activity_statistics.*


class StatisticsActivity : AppCompatActivity() {

    private var placesList : MutableList<StatisticsObject> = mutableListOf()

    private fun parkingName(parking: Parking):String{
        when (parking){
            Parking.C13 -> return "C-13"
            Parking.D20 -> return "D-20"
            Parking.WRO -> return "WRO"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        enumValues<Parking>().forEach {
            var lastStats = ParkingiPwrRepository.getLastWeekStats(it) // check if we have stored values
            if(lastStats != null)
                updateChart(lastStats, parkingName(it))
            else
                ParkingiPwrRepository.getWeekStats(it, object : IParkingResponseObserver {
                    override fun notify(places: Place, parking: Parking) {
                        updateChart(places, parkingName(it))
                    }
                })
        }
    }

    private fun updateChart(parking: Place, parkingName : String){
        placesList.add(StatisticsObject(parkingName, parking.chart))

        val adapter = StatisticsAdapter(this, placesList)
        stats_list_view.adapter = adapter
    }


}
