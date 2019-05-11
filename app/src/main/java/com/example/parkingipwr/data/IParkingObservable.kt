package com.example.parkingipwr.data


/*
    interfejs z funkcją powiadamiającą o uzyskaniu odpowiedzi z endpointa po wykonaniu
    funkcji ParkinigiPwrRepository.getCurrentStatistics()

    parametr: lista wszystkich parkingów
 */

interface IParkingResponseObserver {

    fun notify(parkings: List<Place>)
}

/*
    interfejs z funkcją powiadamiającą o zmianie danych w pewnych parkingach

    paramter: lista parkingów w których zmieniły się dane
 */

interface IParkingChangeObserver{

    fun notifyChanged(changedParkings: List<Place>)
}