package com.jl.barometersensorservice

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.amitshekhar.DebugDB
import com.jl.barometerlibrary.data.BarometerDataContract
import com.jl.barometerlibrary.data.impl.BarometerDataFactoryRoom
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by jl on 3/25/18.
 *
 * Test service using test db
 */
@RunWith(AndroidJUnit4::class)
class GetReadingServiceTest {

    val testDbName = "testBarometerDatabase"

    @Test
    fun runBackgroundService() {
        DebugDB.getAddressLog()
        val getReadingService = GetReadingService()
        getReadingService.dbName = testDbName
        val context = InstrumentationRegistry.getTargetContext()
        val dataContract = BarometerDataFactoryRoom().getTestContract(context, testDbName)
        //dataContract.

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
        if (readings is List<*>) {
            return readings.size
        } else {
            throw IllegalArgumentException()
        }
    }

}