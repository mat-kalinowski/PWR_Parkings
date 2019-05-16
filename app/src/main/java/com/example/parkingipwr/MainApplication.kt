package com.example.parkingipwr

import android.app.Application
import android.content.Context
import com.example.parkingipwr.data.ParkingiPwrRepository

class MainApplication: Application(){

    override fun onCreate(){
        super.onCreate()

        val sp = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        ParkingiPwrRepository.setUp(sp)
    }

}