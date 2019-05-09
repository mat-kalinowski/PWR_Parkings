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





class CurrentParkingsMapActivity : AppCompatActivity(), OnMapReadyCallback {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_parkings_map)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        val mark = LatLng(51.107064, 17.058980)

        val yourLocation = CameraUpdateFactory.newLatLngZoom(mark, 18f)

        googleMap!!.addMarker(
            MarkerOptions().position(mark)
                .title("Marker C-13")
        )
        googleMap!!.animateCamera(yourLocation)
    }
}
