package com.jl.barometersensorservice

import android.content.Intent
import android.support.test.InstrumentationRegistry
import com.amitshekhar.DebugDB
import com.jl.barometerlibrary.data.BarometerDataContract
import com.jl.barometerlibrary.data.impl.BarometerDataFactoryRoom
import junit.framework.Assert.assertEquals
import org.junit.Test
import timber.log.Timber

/**
 * Created by jl on 3/25/18.
 *
 * Test service using test db
 */
class GetReadingServiceTest {

    val testDbName = "testBarometerDatabase"

    @Test
    fun runBackgroundService() {
        Timber.plant(Timber.DebugTree())
        DebugDB.getAddressLog()
        val getReadingService = GetReadingService()
        getReadingService.dbName = testDbName
        val context = InstrumentationRegistry.getTargetContext()
        val dataContract = BarometerDataFactoryRoom().getTestContract(context, testDbName)

        val intent = Intent(context, GetReadingService::class.java)
        val nReadingsBefore = getNumData(dataContract)
        context.startService(intent)

        Thread.sleep(1000)
        val nReadingsAfter = getNumData(dataContract)
        assertEquals(nReadingsBefore + 1, nReadingsAfter)
    }

    private fun getNumData(dataContract: BarometerDataContract): Int {
        val readings = dataContract.getAllData()
                .test()
                .awaitCount(1)
                .assertNoErrors()
                .events[0][0]
        return if (readings is List<*>) {
            readings.size
        } else {
            throw IllegalArgumentException()
        }
    }

}