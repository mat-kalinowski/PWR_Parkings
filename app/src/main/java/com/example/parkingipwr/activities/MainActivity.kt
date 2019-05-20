package com.example.parkingipwr.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.parkingipwr.R
import com.example.parkingipwr.data.*
import com.example.parkingipwr.mock.ParkingBase
import android.net.ConnectivityManager
import android.content.IntentFilter
import android.graphics.Color
import com.example.parkingipwr.connection.ConnectivityReceiver
import com.example.parkingipwr.connection.NetworkStatus
import kotlinx.android.synthetic.main.activity_main.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), IParkingChangeObserver, ConnectivityReceiver.ConnectivityReceiverListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ParkingiPwrRepository.attachObserver(this)

        registerReceiver(ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onResume() {
        super.onResume()

        ConnectivityReceiver.connectivityReceiverListener = this
        showMessage(NetworkStatus.connected)
    }

    override fun notifyChanged(changedParkings: List<Place>){

        val baseArray = ParkingBase.getParkingsArray()
        for(p1 in changedParkings){
            for(p2 in baseArray){
                if(p1.parking_id == p2.id){
                    p2.freePlaces = p1.liczba_miejsc
                }
            }
        }
        SharedPrefs.saveParkingPlacesState(this)
    }


    fun displayCurrentState(view: View){

        val myintent = Intent(this, CurrentParkingsMapActivity::class.java)
        startActivity(myintent)
    }


    fun displayStatistics(view: View){
        val myintent = Intent(this, StatisticsActivity::class.java)
        startActivity(myintent)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        NetworkStatus.connected = isConnected
        showMessage(NetworkStatus.connected)
    }

    private fun showMessage(isConnected: Boolean){

        if(!isConnected){
            textViewAlert.setText(R.string.no_internet_connection_alert)
            textViewAlert.setBackgroundColor(Color.RED)
            textViewAlert.visibility = View.VISIBLE
        }else{
            textViewAlert.text = " "
            textViewAlert.setBackgroundColor(resources.getColor(R.color.colorBackground))
            textViewAlert.visibility = View.GONE
        }
    }


}
