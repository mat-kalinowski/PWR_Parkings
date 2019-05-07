package com.example.parkingipwr

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ParkingiPwrRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://iparking.pwr.edu.pl")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun getParkingPwrStats(){
        val service = retrofit.create(ParkingPwrService::class.java)  // make it object variable (init function?)

        val call = service.getStatus()
        call.enqueue( object : Callback<ParkingPwrResponce> {
            override fun onFailure(call: Call<ParkingPwrResponce>, t: Throwable) {
                Log.d("aaa", "something went wrong, idk what")
            }

            override fun onResponse(call: Call<ParkingPwrResponce>, response: Response<ParkingPwrResponce>) {
                val body = response.body()
                Log.d("aaa", body!!.toString())
                // returns empty "places"
            }

        })
    }
}