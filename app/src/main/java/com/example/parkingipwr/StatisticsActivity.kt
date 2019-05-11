package com.example.parkingipwr

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import android.R.attr.entries
import android.graphics.Color
import android.os.AsyncTask
import android.provider.Settings
import android.util.Log
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class StatisticsActivity : AppCompatActivity() {

    private fun time2float(date : String) : Float {
        var split = date.split(":")
        return split[0].toFloat() + split[1].toFloat()/100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        findViewById<Button>(R.id.button).setOnClickListener {

            AsyncTask.execute {
                val responce = ParkingiPwrRepository.getParkingPwrWeekStatsSync(Parking.C13)
                var chart = findViewById<LineChart>(R.id.chart)
                var entries: MutableList<Entry> = mutableListOf()
                val ResponcechartData = responce!!.places.chart

                for (i in 1 until ResponcechartData.data.size) { // start from 1 due to specific api responce -_-
                    entries.add(Entry(time2float(ResponcechartData.x.get(i)), ResponcechartData.data.get(i).toFloat()))
                }


                val dataSet = LineDataSet(entries, "Label")
                dataSet.color = Color.BLACK

                val lineData = LineData(dataSet)
                chart.data = lineData
                chart.invalidate() // refresh
            }




            }


    }


}
