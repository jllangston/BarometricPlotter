package com.jl.barometerlibrary.sensor

import android.hardware.SensorEvent
import com.jl.barometerlibrary.sensor.BarometerSensorListener
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Created by jl on 1/13/18.
 *
 * Test of sensor listener
 */
class BarometerSensorListenerTest {

    @Test
    fun onSensorChanged() {
        val sensor = BarometerSensorListener()
        val event : SensorEvent? = null
        sensor.onSensorChanged(event)
        assertTrue(sensor.readings.isEmpty())
    }

}