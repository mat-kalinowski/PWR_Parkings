package com.example.parkingipwr.data

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

    var observers : ArrayList<IParkingChangeObserver> = ArrayList()
    var mainThread: ParkingThread = ParkingThread()

    val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    val client : OkHttpClient = OkHttpClient.Builder().apply {
        this.addInterceptor(interceptor)
    }.build()

    var gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .create()


    val retrofit = Retrofit.Builder()
        .baseUrl("http://iparking.pwr.edu.pl")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client) // add to see http request log
        .build()

    init{
        mainThread.setRunning(true)
        mainThread.start()
    }

    @Synchronized
    fun getCurrentStats(observer: IParkingResponseObserver){

        val service = retrofit.create(ParkingPwrService::class.java)  // make it object variable (init function?)
        val call = service.getStatus()
        call.enqueue( object : Callback<ParkingPwrResponce> {
            override fun onFailure(call: Call<ParkingPwrResponce>, t: Throwable) {
                Log.e("DATA ERROR: ", "something went wrong, idk what \n" + t.message)
            }

            override fun onResponse(call: Call<ParkingPwrResponce>, response: Response<ParkingPwrResponce>) {
                val body = response.body()
                observer.notify(body!!.places)
            }
        })
    }

    @Synchronized
    fun getWeekStatistics(parking: Parking): Place{
        val service = retrofit.create(ParkingPwrService::class.java)  // make it object variable (init function?)
        var place: Place = Place()

        val a = object: Thread() {
            override fun run() {
                val requestBody = "o=get_park&i="+parking.value.toString()
                val call = service.getWeekStatus(requestBody)
                val response =  call.execute()

                place = response.body()!!.places
            }
        }
        a.start()
        a.join()

        return place
    }

    fun attachObserver(observer: IParkingChangeObserver){
        this.observers.add(observer)
    }

    fun notifyObservers(changedParking: List<Place>){
        for(i in 0 .. (observers.size-1)){
            observers[i].notifyChanged(changedParking)
        }
    }

}