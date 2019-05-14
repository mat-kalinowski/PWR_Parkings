package com.example.parkingipwr.data

import com.example.parkingipwr.data.Place


data class ParkingWeekResponse(
    var success : Int,
    var places : Place
)

data class ParkingPwrResponse(
    var success : Int,
    var places : List<Place>
)