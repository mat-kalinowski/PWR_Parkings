package com.example.parkingipwr.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.parkingipwr.mock.ParkingBase
import java.lang.Exception

object SharedPrefs{
    private const val PREFS_NAME = "parking_preferences"
    private const val SAVED_PARKING_STATE = "saved_parking_state"


    fun saveParkingPlacesState(context: Context){

        val parkingInfos = ParkingBase.getParkingsArray()
        var s = ""
        for(p in parkingInfos){
            s += p.id.toString() + "," + p.freePlaces + ";"
        }
        val sharedPref : SharedPreferences = context.getSharedPreferences( PREFS_NAME, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPref.edit()
        editor.putString(SAVED_PARKING_STATE, s)
        editor!!.commit()
    }

    private fun getSavedParkingsStateString(context: Context) : String{
        val sharedPref : SharedPreferences = context.getSharedPreferences( PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(SAVED_PARKING_STATE, "")
    }

    fun loadSavedParkingState(context: Context){
        val array = getSavedParkingsStateString(context).split(";").toTypedArray()
        for(i in array){
            try{
                val array2 = i.split(",").toTypedArray()
                ParkingBase.updateFreePlacesCountForId(array2[0].toInt(), array2[1].toInt())
            }catch (e : Exception){
                Log.e("EXC" , e.toString())
            }

        }
    }

}