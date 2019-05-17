package com.example.parkingipwr.activities

import android.app.Dialog
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.TextureView
import android.view.View
import android.view.Window
import android.widget.TextView
import com.example.parkingipwr.mock.ParkingBase
import com.example.parkingipwr.mock.ParkingInfo
import com.example.parkingipwr.R
import com.example.parkingipwr.connection.ConnectivityReceiver
import com.example.parkingipwr.connection.NetworkStatus
import com.example.parkingipwr.data.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.ui.IconGenerator
import com.yarolegovich.lovelydialog.LovelyInfoDialog
import kotlinx.android.synthetic.main.activity_current_parkings_map.*
import java.util.*


class CurrentParkingsMapActivity :  AppCompatActivity(), OnMapReadyCallback,
                                    GoogleMap.OnMarkerClickListener, IParkingChangeObserver,
                                    ConnectivityReceiver.ConnectivityReceiverListener {

    private lateinit var iconFactory : IconGenerator
    private lateinit var mMap: GoogleMap
    private var parkingMarkers = ArrayList<Marker>()
    private var parkingPlaces = ArrayList<Place>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_parkings_map)

        registerReceiver(ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        iconFactory = IconGenerator(this)
        iconFactory.setStyle(IconGenerator.STYLE_ORANGE)
        iconFactory.setTextAppearance(R.style.MapMarkerText)

        ParkingiPwrRepository.attachObserver(this)
        ParkingiPwrRepository.getCurrentStats(object : IParkingResponseObserver {
            override fun notify(parkings: List<Place>){
                Log.e("hey", parkings.size.toString())
            }
        })

        ParkingBase.loadSavedDataIfEmpty(this)

        for(i in 0 until ParkingBase.getParkingsArray().size){
            var p = ParkingBase.getParkingsArray()[i]
            parkingPlaces.add(Place(0, p.id, Date(), p.freePlaces, 0, Chart()))
        }
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
        showMessage(NetworkStatus.connected)
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        googleMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_total_grey))
        var mark = LatLng(51.109353,17.058040)                                                          //TODO
        mMap = googleMap!!
        val location = CameraUpdateFactory.newLatLngZoom(mark, 15.5f)
        mMap.setOnMarkerClickListener(this)

        if(parkingPlaces.size > 0)
            addMarkers()
        mMap.animateCamera(location)
    }

    override fun notifyChanged(changedParkings: List<Place>) {
        Log.e("NOTIFYCURR: ", changedParkings.size.toString())

        for(p1 in changedParkings){
            for(p2 in parkingPlaces){
                if(p1.parking_id == p2.parking_id){
                    parkingPlaces[parkingPlaces.indexOf(p2)]= p1
                }
            }
        }
        for(m in parkingMarkers){
            m.remove()
        }
        addMarkers()
    }


    fun addMarkers(){
        for(i in 0 until parkingPlaces.size){
            var p = parkingPlaces.get(i)
            var parkingInfo = ParkingBase.getParkingFromID(p.parking_id)
            if(parkingInfo != null){
                val text : String = p.liczba_miejsc.toString() + "/" + parkingInfo.availablePlaces
                val markerOptions =
                    MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).position(parkingInfo.latLng)
                        .anchor(iconFactory.anchorU, iconFactory.anchorV)
                var marker = mMap.addMarker(markerOptions)

                if(parkingMarkers.size == i){
                    parkingMarkers.add(marker)
                }else{
                    parkingMarkers[i] = marker
                }
            }
        }
    }

    override fun onMarkerClick(clickedMarker: Marker?): Boolean {

        for(i in 0 until parkingMarkers.size){
            if(clickedMarker!!.equals(parkingMarkers.get(i))){
                var parking = ParkingBase.getParkingFromID(parkingPlaces[i].parking_id)

                val dialog = Dialog(this)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.parking_info_dialog)
                (dialog.findViewById(R.id.textViewTitle) as TextView).setText(parking!!.getNameString())
                (dialog.findViewById(R.id.textViewDescription) as TextView).setText(parking!!.getDescriptionString())
                dialog.show()
            }
        }
        return true
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        NetworkStatus.connected = isConnected
        showMessage(isConnected)
    }

    fun showMessage(isConnected: Boolean){
        Log.d("network2", isConnected.toString())
        if(!isConnected){
            textViewAlert.setText(R.string.no_internet_connection_alert)
            textViewAlert.setBackgroundColor(Color.RED)
            textViewAlert.visibility = View.VISIBLE
        }else{
            textViewAlert.setText(" ")
            textViewAlert.setBackgroundColor(resources.getColor(R.color.colorBackground))
            textViewAlert.visibility = View.GONE
        }
    }


}
