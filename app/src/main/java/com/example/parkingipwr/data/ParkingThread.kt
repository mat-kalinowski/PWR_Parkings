package com.example.parkingipwr.data

class ParkingThread : Thread(), IParkingResponseObserver {

    private var lastStats: MutableList<Place> = mutableListOf()
    private var lastWeekStats = mutableMapOf<Parking, Place>()

    override fun run() {
        // evaluate only once
        enumValues<Parking>().forEach {
            ParkingiPwrRepository.getWeekStats(it, this)
        }
        while (true) {
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

    override fun notify(places: Place, parking: Parking) {
        lastWeekStats[parking] = places
    }




    @Synchronized
    fun getLastStats(): List<Place>{
        return this.lastStats.toMutableList()
    }

    @Synchronized
    fun getLastWeekStats(parking: Parking): Place?{
        return this.lastWeekStats[parking]
    }
}