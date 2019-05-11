package com.example.parkingipwr.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.parkingipwr.mock.ParkingBase
import com.example.parkingipwr.mock.ParkingInfo
import com.example.parkingipwr.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.ui.IconGenerator
import com.yarolegovich.lovelydialog.LovelyInfoDialog


class CurrentParkingsMapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    lateinit var iconFactory : IconGenerator
    lateinit var mMap: GoogleMap
    var parkingInfos = ArrayList<ParkingInfo>()
    var parkingMarkers = ArrayList<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_parkings_map)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        iconFactory = IconGenerator(this)
        iconFactory.setStyle(IconGenerator.STYLE_ORANGE)
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        var mark = LatLng(51.109353,17.058040)
        mMap = googleMap!!
        val location = CameraUpdateFactory.newLatLngZoom(mark, 15f)
        mMap.setOnMarkerClickListener(this)
        val parkingsArray = ParkingBase.getParkingsArray()

        for(i in 0 until parkingsArray.size){
            addMarker(parkingsArray.get(i))
        }
        mMap.animateCamera(location)


    }


   /* fun addMarker(name : String, latLng: LatLng, freePlaces : Int, availablePlaces : Int){
        val text : String = "$freePlaces/$availablePlaces"
        val markerOptions =
            MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).position(latLng)
                .anchor(iconFactory.anchorU, iconFactory.anchorV)


        var m = mMap.addMarker(markerOptions)
        parkingInfos.add(ParkingInfo(name, latLng,availablePlaces, freePlaces))
        parkingMarkers.add(m)

    }*/

    fun addMarker(parkingInfo: ParkingInfo){
        val text : String = "${parkingInfo.freePlaces}/${parkingInfo.availablePlaces}"
        val markerOptions =
            MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).position(parkingInfo.latLng)
                .anchor(iconFactory.anchorU, iconFactory.anchorV)

        var m = mMap.addMarker(markerOptions)
        parkingInfos.add(parkingInfo)
        parkingMarkers.add(m)
    }

    override fun onMarkerClick(clickedMarker: Marker?): Boolean {

        for(i in 0 until parkingMarkers.size){
            if(clickedMarker!!.equals(parkingMarkers.get(i))){
                var parking = parkingInfos.get(i)

                LovelyInfoDialog(this)
                    .setTopColorRes(R.color.colorBackground)
                    .setIcon(parking.icon)
                    .setTitle(parking.getNameString())
                    .setMessage(parking.getDescriptionString())
                    .show()
            }
        }
        return true
    }



}
