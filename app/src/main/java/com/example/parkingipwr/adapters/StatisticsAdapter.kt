package com.example.parkingipwr.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
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
import com.github.mikephil.charting.utils.ColorTemplate
import org.w3c.dom.Text
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet




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
        dataSet.valueTextColor = R.color.colorBackground
        dataSet.isHighlightEnabled = false

        val lineData = BarData(dataSet)
        var colorTemplate: IntArray = IntArray(3)
        colorTemplate[0] = R.color.barChartColor1
        colorTemplate[1] = R.color.barChartColor2
        colorTemplate[2] = R.color.barChartColor3
        dataSet.setColors(colorTemplate, context)

        val chartObj = statisticItem.findViewById<BarChart>(R.id.chart)
        chartObj.data = lineData

        chartObj.invalidate()
        chartObj.description.text = "ilość miejsc"
        chartObj.barData.setValueTextColor(context.resources.getColor(R.color.colorText))
        chartObj.description.textColor = context.resources.getColor(R.color.colorBackground)
        chartObj.setBackgroundColor(context.resources.getColor(R.color.colorText))

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