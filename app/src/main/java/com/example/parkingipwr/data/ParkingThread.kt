package com.example.parkingipwr.data

class ParkingThread : Thread(), IParkingResponseObserver {

    private var lastStats: MutableList<Place> = mutableListOf()

    override fun run() {

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

    @Synchronized
    fun getLastStats(): List<Place>{
        return this.lastStats.toMutableList()
    }
}