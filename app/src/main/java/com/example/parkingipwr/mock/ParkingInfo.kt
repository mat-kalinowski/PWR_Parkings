package com.example.parkingipwr.mock

import com.example.parkingipwr.R
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class ParkingInfo(val id: Int, val name : String, val latLng: LatLng, val availablePlaces : Int ){

    var freePlaces = 0

    fun getNameString(): String{
        return "Parking $name"
    }

    fun getDescriptionString() : String{
        return "Wolne miejsca: $freePlaces \nDostępne miejsca: $availablePlaces \n "
    }

}