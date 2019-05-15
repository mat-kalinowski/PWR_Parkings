package com.example.parkingipwr.mock

import android.content.Context
import com.example.parkingipwr.data.Parking
import com.example.parkingipwr.data.SharedPrefs
import com.google.android.gms.maps.model.LatLng

object ParkingBase{

    private val base = ArrayList<ParkingInfo>()
    init {
        base.add(
            ParkingInfo(
                Parking.WRO.value,
                "WRO",
                LatLng(51.108964, 17.055564),
                207
            )
        )
        base.add(
            ParkingInfo(
                Parking.C13.value,
                "C-13",
                LatLng(51.107393, 17.058468),
                54
            )
        )
        base.add(
            ParkingInfo(
                Parking.D20.value,
                "D-20",
                LatLng(51.1100504, 17.0596779),
                76
            )
        )
    }


    fun loadSavedDataIfEmpty(context: Context){
        if(freePalcesSum() == 0){
            SharedPrefs.loadSavedParkingState(context)
        }
    }


    fun getParkingsArray() : ArrayList<ParkingInfo>{
        return base
    }

    fun getParkingFromID(id: Int) : ParkingInfo? {
        for(i in base){
            if(i.id == id)
                return i
        }
        return null
    }

    fun updateFreePlacesCountForId(id : Int, count : Int){
        for(i in base){
            if(i.id == id)
                i.freePlaces = count
        }
    }

    fun freePalcesSum() :Int{
        var sum = 0
        for(p in base){
            sum += p.freePlaces
        }
        return sum
    }
}