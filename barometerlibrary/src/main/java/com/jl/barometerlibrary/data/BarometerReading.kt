package com.jl.barometerlibrary.data

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by jl on 1/27/18.
 *
 * Barometer data class
 */
data class BarometerReading(val reading: Double, val time: Long = System.currentTimeMillis()) {

    private val format = SimpleDateFormat("dd-MM:HH-mm-ss.SSS", Locale.ENGLISH)

    private fun getDate(ms: Long) : String {
        val date = Date(ms)
        return format.format(date)
    }

    override fun toString(): String {
        val date = getDate(time)
        return "BarometerReading(reading=$reading, timestamp=$date)"
    }

    constructor(reading: Float) : this(reading.toDouble())
}
