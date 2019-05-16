package com.example.parkingipwr.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import java.util.concurrent.TimeUnit


class ParkingThread : Thread(), IParkingResponseObserver {

    private var lastStats: MutableList<Place> = mutableListOf()
    private var lastWeekStats = mutableMapOf<Parking, Place>()

    private var loadedFromSP = false
    private var isRunning = true

    private var sharedPreferences: SharedPreferences? = null
    private var gson = Gson()

    override fun run() {

        if (!loadedFromSP) {
            enumValues<Parking>().forEach {
                ParkingiPwrRepository.getWeekStats(it, this)
            }
        }

        while (isRunning) {
            ParkingiPwrRepository.getCurrentStats(this)

            Thread.sleep(20000)
        }
    }

    @Synchronized
    override fun notify(parkings: List<Place>) {
        var changedParkings = mutableListOf<Place>()

        if (parkings.size != lastStats.size) {
            lastStats = parkings.toMutableList()
            changedParkings = parkings.toMutableList()
        }

        else {
            for (i in 0..(parkings.size - 1)) {

                if (parkings[i].liczba_miejsc != lastStats[i].liczba_miejsc) {
                    lastStats[i] = parkings[i]
                    changedParkings.add(parkings[i])
                }
            }
        }

        if (changedParkings.size != 0) {
            ParkingiPwrRepository.notifyObservers(changedParkings)
        }
    }

    override fun notify(places: Place, parking: Parking){
        lastWeekStats[parking] = places

        if(lastWeekStats.size == enumValues<Parking>().size){
            saveInSP()
        }
    }

    @Synchronized
    fun getLastStats(): List<Place>{
        return this.lastStats.toMutableList()
    }

    @Synchronized
    fun getLastWeekStats(parking: Parking): Place?{
        return this.lastWeekStats[parking]
    }

    fun loadFromSP(sp: SharedPreferences){
        sharedPreferences = sp

        val spDate = sp.getLong("WEEK_DATE", 0)
        val diff = Date().time - spDate

        if(TimeUnit.MILLISECONDS.toDays(diff) < 7){
            loadedFromSP = true

            val json = sp.getString("WEEK_DATA","")
            val mapType = object : TypeToken<MutableMap<Parking,Place>>() { }.type

            lastWeekStats = gson.fromJson(json, mapType)
        }
    }

    private fun saveInSP(){
        if(!loadedFromSP && sharedPreferences != null){
            val editor = sharedPreferences!!.edit()
            editor.putLong("WEEK_DATE", Date().time)

            val json = gson.toJson(lastWeekStats)
            editor.putString("WEEK_DATA", json)

            editor.apply()
        }
    }
}