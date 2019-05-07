package com.example.parkingipwr

import java.util.*

data class ParkingPwrResponce(
    var success : Int,
    var places : List<Place>
)

data class Place(
    var id : Long,
    var parking_id : Int,
    var czas_pomiaru : Date,
    var liczba_miejsc : Int,
    var trend : Int,
    var chart : Chart
)

data class Chart(
    /*
    DUMBASS that created this api returns array of mixed types...
    for x response is like "x" : [ "x" , date1, date2, ...]
    for god sake WHY just WHY did she/he included "x" ?!?!?1

    same for data.
     */
    var x : List<String>,
    var data : List<String>
)