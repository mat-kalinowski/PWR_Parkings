package com.example.parkingipwr.data


interface IParkingResponseObserver {

    /*
        funkcja powiadamiająca o uzyskaniu odpowiedzi z endpointa po wykonaniu funkcji
        ParkingiPwrRepository.getCurrentStats()

        parkings: lista tygodniowych danych o wszystkich parkingach
     */

    fun notify(parkings: List<Place>){}

    /*
        funkcja powiadamiająca o uzyskaniu odpowiedzi z endpointa po wykonaniu funkcji
        ParkingiPwrRepository.getWeekStats()

        parking: dane parkingu, którego dotyczyło zapytanie
     */

    fun notify(parking: Place){}
}

interface IParkingChangeObserver{

    /*
        funkcja powiadamiającą o zmianie danych w pewnych parkingach

        changedParkings: lista parkingów w których zmieniły się dane
    */

    fun notifyChanged(changedParkings: List<Place>)
}