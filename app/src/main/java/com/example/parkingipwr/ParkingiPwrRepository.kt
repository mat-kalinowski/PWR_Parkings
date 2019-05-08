package com.example.parkingipwr

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import com.google.gson.GsonBuilder


object ParkingiPwrRepository {

    val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    val client : OkHttpClient = OkHttpClient.Builder().apply {
        this.addInterceptor(interceptor)
    }.build()

    var gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .create()


    private val retrofit = Retrofit.Builder()
        .baseUrl("http://iparking.pwr.edu.pl")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client) // add to see http request log
        .build()


    fun getParkingPwrStats(){
        val service = retrofit.create(ParkingPwrService::class.java)  // make it object variable (init function?)

        val call = service.getStatus()
        call.enqueue( object : Callback<ParkingPwrResponce> {
            override fun onFailure(call: Call<ParkingPwrResponce>, t: Throwable) {
                Log.d("aaa", "something went wrong, idk what \n" + t.message)
            }

            override fun onResponse(call: Call<ParkingPwrResponce>, response: Response<ParkingPwrResponce>) {
                val body = response.body()
                Log.d("aaa", body!!.toString())
            }

        })
    }
}