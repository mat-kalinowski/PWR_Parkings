package com.example.parkingipwr.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.parkingipwr.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet




@Suppress("DEPRECATION")
class StatisticsAdapter(private val context: Context, private val data : MutableList<StatisticsObject>) : BaseAdapter(){

    private fun time2float(date : String) : Float {
        val split = date.split(":")
        return split[0].toFloat() + split[1].toFloat()/100
    }


    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var lastFreeSlot = ""
        val statisticItem = inflater.inflate(R.layout.statistic_item, parent, false)

        statisticItem.findViewById<TextView>(R.id.firstLine).text = (getItem(position) as StatisticsObject).parkingName

        val entries: MutableList<BarEntry> = mutableListOf()
        val responcechartData = (getItem(position) as StatisticsObject).chart

        for (i in 1 until responcechartData.data.size) { // start from 1 due to specific api responce -_-
            if(responcechartData.data[i] == "0" && lastFreeSlot == ""){
                val split = responcechartData.x[i].split(":")
                lastFreeSlot = split[0] + ":" + split[1]
            }

            entries.add(
                BarEntry(
                    time2float(responcechartData.x[i]),
                    responcechartData.data[i].toFloat()
                )
            )
        }


        val dataSet = BarDataSet(entries, "")
        dataSet.color = R.color.colorText
        dataSet.valueTextColor = R.color.colorBackground
        dataSet.isHighlightEnabled = false

        val lineData = BarData(dataSet)
        val colorTemplate= IntArray(3)
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