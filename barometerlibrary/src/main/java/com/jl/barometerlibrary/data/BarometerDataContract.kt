package com.jl.barometerlibrary.data

import io.reactivex.Flowable

/**
 * Created by jl on 2/3/18.
 *
 * Contract specifying barometer data
 */
interface BarometerDataContract {

    fun addReading(reading: BarometerReading)
    fun getAllData() : Flowable<List<BarometerReading>>

}