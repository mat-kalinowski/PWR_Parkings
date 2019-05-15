package com.example.parkingipwr.data

import java.time.LocalDateTime
import java.util.*

data class Place(
    var id : Long = 0,
    var parking_id : Int = 0,
    var czas_pomiaru : Date = Date(0),
    var liczba_miejsc : Int = 0,
    var trend : Int = 0,
    var chart : Chart = Chart()
)


data class Chart(
    /*
    DUMBASS that created this api returns array of mixed types...
    for x response is like "x" : [ "x" , date1, date2, ...]
    for god sake WHY just WHY did she/he included "x" ?!?!?1

    same for data.
     */
    var x : List<String> = emptyList(),
    var data : List<String> = emptyList()
)