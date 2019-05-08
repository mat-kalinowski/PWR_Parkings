package com.example.parkingipwr

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ParkingPwrService {

    @Headers(
        "Origin:http://iparking.pwr.edu.pl",
        "User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36",
        "Referer:http://iparking.pwr.edu.pl/?",
        "X-Requested-With:XMLHttpRequest",
        "Connection:keep-alive",
        "Content-Type:application/x-www-form-urlencoded"
    )
    @POST("/modules/iparking/scripts/ipk_operations.php")
    fun getStatus(@Body body : String = "o=get_parks") : Call<ParkingPwrResponce>
}