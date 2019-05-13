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
    var parkingThread: ParkingThread = ParkingThread()

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
        parkingThread.start()
    }

    /*
        funkcja wykonująca zapytanie do API o dane odnośnie wszystkich parkingów

        observer: obiekt implementujący IParkingResponseObserver, który zostaje powiadomiony o wyniku metodą notify()
     */

    @Synchronized
    fun getCurrentStats(observer: IParkingResponseObserver){

        val service = retrofit.create(ParkingPwrService::class.java)  // make it object variable (init function?)
        val call = service.getStatus()
        call.enqueue( object : Callback<ParkingPwrResponse> {
            override fun onFailure(call: Call<ParkingPwrResponse>, t: Throwable) {
                Log.e("DATA ERROR: ", "something went wrong, idk what \n" + t.message)
            }

            override fun onResponse(call: Call<ParkingPwrResponse>, response: Response<ParkingPwrResponse>) {
                val body = response.body()
                observer.notify(body!!.places)
            }
        })
    }

    /*
        funkcja zwracająca ostatnie dane o parkingach uzyskane z API
     */

    fun getLastStats() : List<Place>{
        return parkingThread.getLastStats()
    }

    /*
        funkcja wykonująca zapytanie do API o tygodniowe dane parkingu

        parking: parking, którego dotyczy zapytanie
        observer: obiekt implementujący IParkingResponseObserver, który zostaje powiadomiony o wyniku metodą notify()
     */

    @Synchronized
    fun getWeekStats(parking: Parking, observer: IParkingResponseObserver){
        val service = retrofit.create(ParkingPwrService::class.java)  // make it object variable (init function?)
        val requestBody = "o=get_park&i="+parking.value.toString()
        val call = service.getWeekStatus(requestBody)

        call.enqueue( object : Callback<ParkingWeekResponse> {
            override fun onFailure(call: Call<ParkingWeekResponse>, t: Throwable) {
                Log.e("DATA ERROR: ", "something went wrong, idk what \n" + t.message)
            }

            override fun onResponse(call: Call<ParkingWeekResponse>, response: Response<ParkingWeekResponse>) {
                val body = response.body()
                observer.notify(body!!.places, parking)
            }
        })
    }

    fun getLastWeekStats(parking : Parking) : Place? {
        return parkingThread.getLastWeekStats(parking)
    }

    /*
        funkcja, poprzez którą klasa implementująca IParkingChangeObserve może zapisać się do uzyskiwania powiadomień
        o zmianach danych w parkingach

        observer: obiekt implementujący IParkingChangeObserver, który zostaje powiadomiony o każdej zmianie metodą notifyChanged()
     */

    @Synchronized
    fun attachObserver(observer: IParkingChangeObserver){
        this.observers.add(observer)
    }

    @Synchronized
    fun notifyObservers(changedParking: List<Place>){
        for(i in 0 .. (observers.size-1)){
            observers[i].notifyChanged(changedParking)
        }
    }

}