package com.example.parkingipwr.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import android.graphics.Color
import com.example.parkingipwr.data.Parking
import com.example.parkingipwr.data.ParkingiPwrRepository
import com.example.parkingipwr.R
import com.example.parkingipwr.data.IParkingResponseObserver
import com.example.parkingipwr.data.Place
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData


class StatisticsActivity : AppCompatActivity() {

    private fun time2float(date : String) : Float {
        var split = date.split(":")
        return split[0].toFloat() + split[1].toFloat()/100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        var lastStats = ParkingiPwrRepository.getLastWeekStats(Parking.C13) // check if we have stored values
        if(lastStats != null)
            updateChart(lastStats)
        else
            ParkingiPwrRepository.getWeekStats(Parking.C13, object : IParkingResponseObserver {
                override fun notify(places: Place, parking: Parking) {
                    updateChart(places)
                }
            })
    }

    private fun updateChart(parking: Place){

        var chart = findViewById<LineChart>(R.id.chart)
        var entries: MutableList<Entry> = mutableListOf()
        val ResponcechartData = parking.chart

        for (i in 1 until ResponcechartData.data.size) { // start from 1 due to specific api responce -_-
            entries.add(
                Entry(
                    time2float(ResponcechartData.x.get(i)),
                    ResponcechartData.data.get(i).toFloat()
                )
            )
        }

        val dataSet = LineDataSet(entries, "Label")
        dataSet.color = Color.BLACK

        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.invalidate() // refresh
    }


}
