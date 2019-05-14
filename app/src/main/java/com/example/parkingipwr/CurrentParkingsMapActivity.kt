package com.example.parkingipwr

import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.TextureView
import android.view.Window
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.model.*
import com.google.maps.android.ui.IconGenerator
import com.yarolegovich.lovelydialog.LovelyInfoDialog


class CurrentParkingsMapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private lateinit var iconFactory : IconGenerator
    private lateinit var mMap: GoogleMap
    private var parkingInfos = ArrayList<ParkingInfo>()
    private var parkingMarkers = ArrayList<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_parkings_map)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        iconFactory = IconGenerator(this)
        iconFactory.setStyle(IconGenerator.STYLE_ORANGE)
        iconFactory.setTextAppearance(R.style.MapMarkerText)
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        googleMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_total_grey))
        var mark = LatLng(51.109353,17.058040)
        mMap = googleMap!!
        val location = CameraUpdateFactory.newLatLngZoom(mark, 15.5f)
        mMap.setOnMarkerClickListener(this)
        val parkingsArray = ParkingBase.getParkingsArray()

        for(i in 0 until parkingsArray.size){
            addNewMapMarker(parkingsArray.get(i))
        }
        mMap.animateCamera(location)
        updateMapMarkerForParking(1,12)


    }

    fun updateMapMarkerForParking(id : Int, freeParkingLotsNumber : Int){
        (parkingMarkers[id] as Marker).remove()
        parkingInfos[id].freePlaces = freeParkingLotsNumber
        parkingMarkers[id] = addMarker(parkingInfos[id])
    }


    fun addNewMapMarker(parkingInfo: ParkingInfo){
        parkingInfos.add(parkingInfo)
        parkingMarkers.add(addMarker(parkingInfo))
    }

    fun addMarker(parkingInfo: ParkingInfo) : Marker{
        val text : String = "${parkingInfo.freePlaces}/${parkingInfo.availablePlaces}"
        val markerOptions =
            MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).position(parkingInfo.latLng)
                .anchor(iconFactory.anchorU, iconFactory.anchorV)

        return mMap.addMarker(markerOptions)

    }

    override fun onMarkerClick(clickedMarker: Marker?): Boolean {

        for(i in 0 until parkingMarkers.size){
            if(clickedMarker!!.equals(parkingMarkers.get(i))){
                var parking = parkingInfos.get(i)

                /*LovelyInfoDialog(this)
                    .setTopColorRes(R.color.colorBackground)
                    .setTitle(parking.getNameString())
                    .setTopTitleColor(R.color.colorText)
                    .setTitleGravity(Gravity.LEFT)
                    .setMessageGravity(Gravity.CENTER)
                    .setIcon(parking.icon)
                    .setMessage(parking.getDescriptionString())
                    .show()*/
                val dialog = Dialog(this)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.parking_info_dialog)
                (dialog.findViewById(R.id.textViewTitle) as TextView).setText(parking.getNameString())
                (dialog.findViewById(R.id.textViewDescription) as TextView).setText(parking.getDescriptionString())
                dialog.show()
            }
        }
        return true
    }



}
