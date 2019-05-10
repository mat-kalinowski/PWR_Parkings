package com.example.parkingipwr

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.ui.IconGenerator


class CurrentParkingsMapActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var iconFactory : IconGenerator
    lateinit var mMap: GoogleMap

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
        addMarker(LatLng( 51.108964, 17.055564), 197,207)
        addMarker(LatLng( 51.107393, 17.058468), 50,54)
        addMarker(LatLng( 51.1100504, 17.0596779), 56,76)
        mMap.animateCamera(location)


    }


    fun addMarker(latLng: LatLng, freePlaces : Int, availablePlaces : Int){

        val text : String = "$freePlaces/$availablePlaces"
        val markerOptions =
            MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).position(latLng)
                .anchor(iconFactory.anchorU, iconFactory.anchorV)

        mMap.addMarker(markerOptions)
    }

}
