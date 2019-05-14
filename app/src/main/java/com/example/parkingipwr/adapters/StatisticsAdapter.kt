package com.example.parkingipwr.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.parkingipwr.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import org.w3c.dom.Text

class StatisticsAdapter(private val context: Context, private val data : MutableList<StatisticsObject>) : BaseAdapter(){

    private fun time2float(date : String) : Float {
        var split = date.split(":")
        return split[0].toFloat() + split[1].toFloat()/100
    }


    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var lastFreeSlot = ""
        var statisticItem = inflater.inflate(R.layout.statistic_item, parent, false)

        statisticItem.findViewById<TextView>(R.id.firstLine).text = (getItem(position) as StatisticsObject).parkingName

        var entries: MutableList<BarEntry> = mutableListOf()
        val ResponcechartData = (getItem(position) as StatisticsObject).chart

        for (i in 1 until ResponcechartData.data.size) { // start from 1 due to specific api responce -_-
            if(ResponcechartData.data.get(i) == "0" && lastFreeSlot == ""){
                var split = ResponcechartData.x.get(i).split(":")
                lastFreeSlot = split[0] + ":" + split[1]
            }

            entries.add(
                BarEntry(
                    time2float(ResponcechartData.x.get(i)),
                    ResponcechartData.data.get(i).toFloat()
                )
            )
        }

        val dataSet = BarDataSet(entries, "")
        dataSet.color = R.color.colorText
        dataSet.isHighlightEnabled = false

        val lineData = BarData(dataSet)

        val chartObj = statisticItem.findViewById<BarChart>(R.id.chart)
        chartObj.data = lineData
        chartObj.invalidate()
        chartObj.description.text = "ilość miejsc"

        statisticItem.findViewById<TextView>(R.id.thirdLine).text = lastFreeSlot

        return  statisticItem
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }

}