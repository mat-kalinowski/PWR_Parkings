package com.example.parkingipwr.activities

import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Color
import android.net.ConnectivityManager
import android.view.View
import com.example.parkingipwr.R.*
import com.example.parkingipwr.adapters.StatisticsAdapter
import com.example.parkingipwr.adapters.StatisticsObject
import com.example.parkingipwr.connection.ConnectivityReceiver
import com.example.parkingipwr.connection.NetworkStatus
import com.example.parkingipwr.data.*
import kotlinx.android.synthetic.main.activity_statistics.*


@Suppress("DEPRECATION")
class StatisticsActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private var placesList : MutableList<StatisticsObject> = mutableListOf()

    private fun parkingName(parking: Parking):String{
        return when (parking){
            Parking.C13 -> "C-13"
            Parking.D20 -> "D-20"
            Parking.WRO -> "WRO"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_statistics)

        enumValues<Parking>().forEach {
            val lastStats = ParkingiPwrRepository.getLastWeekStats(it) // check if we have stored values
            if(lastStats != null)
                updateChart(lastStats, parkingName(it))
            else
                ParkingiPwrRepository.getWeekStats(it, object : IParkingResponseObserver {
                    override fun notify(places: Place, parking: Parking) {
                        updateChart(places, parkingName(it))
                    }
                })
        }

        registerReceiver(ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onResume() {
        super.onResume()

        ConnectivityReceiver.connectivityReceiverListener = this
        showMessage(NetworkStatus.connected)
    }

    private fun updateChart(parking: Place, parkingName : String){
        placesList.add(StatisticsObject(parkingName, parking.chart))

        val adapter = StatisticsAdapter(this, placesList)
        stats_list_view.adapter = adapter
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        NetworkStatus.connected = isConnected
        showMessage(isConnected)
    }

    private fun showMessage(isConnected: Boolean){
        if(!isConnected){
            textViewAlert.setText(string.no_internet_connection_alert)
            textViewAlert.setBackgroundColor(Color.RED)
            textViewAlert.visibility = View.VISIBLE
        }else{
            textViewAlert.text = " "
            textViewAlert.setBackgroundColor(resources.getColor(color.colorBackground))
            textViewAlert.visibility = View.GONE
        }
    }

}
