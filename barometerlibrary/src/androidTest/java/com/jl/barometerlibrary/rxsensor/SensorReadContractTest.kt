package com.jl.barometerlibrary.rxsensor

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.jl.barometerlibrary.data.BarometerReading
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber

/**
 * Created by jl on 3/18/18.
 *
 * Test sensor reads
 */
@RunWith(AndroidJUnit4::class)
class SensorReadContractTest {

    //val logger = LoggerFactory.getLogger(SensorReadContract::class.java)
    private lateinit var readContract: SensorReadContract

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getTargetContext()
        readContract = SensorReadFactory().getContract(context)
        Timber.plant(Timber.DebugTree())
    }

    @Test
    fun getPressureReading() {
        val reading1 = getReading()
        val reading2 = getReading()

        assertTrue(reading2.time > reading1.time)
        assertNotEquals(reading1, reading2)

    }

    fun getReading() : BarometerReading {
        val time0 = System.nanoTime()
        val data = readContract.getPressureReading(SensorReadContract.N_READINGS_TO_AVERAGE)
                //.test()
                .subscribeOn(Schedulers.computation())
                .blockingFirst()
        val timeDiff = System.nanoTime() - time0
        println("reading: $data: in time: $timeDiff")
        return data
    }

}