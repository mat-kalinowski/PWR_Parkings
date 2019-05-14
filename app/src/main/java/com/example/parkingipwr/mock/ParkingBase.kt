package com.example.parkingipwr.mock

import com.google.android.gms.maps.model.LatLng

object ParkingBase{

    private val base = ArrayList<ParkingInfo>()
    init {
        base.add(
            ParkingInfo(
                0,
                "WRO",
                LatLng(51.108964, 17.055564),
                207
            )
        )
        base.add(
            ParkingInfo(
                1,
                "C-13",
                LatLng(51.107393, 17.058468),
                54
            )
        )
        base.add(
            ParkingInfo(
                2,
                "D-20",
                LatLng(51.1100504, 17.0596779),
                76
            )
        )
    }


    fun getParkingsArray() : ArrayList<ParkingInfo>{
        return base
    }

    fun getParkingFromID(id: Int) : ParkingInfo {
        return base[id]
    }

    fun setFreePlacesForID (id: Int, freePlaces : Int){
        base[id].freePlaces = freePlaces
    }
}