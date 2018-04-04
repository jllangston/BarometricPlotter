package com.jl.barometersensorservice

import android.support.test.InstrumentationRegistry
import com.amitshekhar.DebugDB
import org.junit.Test

import timber.log.Timber

/**
 * Long running test to verify scheduling of service
 *
 * Created by jl on 3/31/18.
 */
class ReadingJobServiceTest {

    @Test
    fun onStartJob() {
        Timber.plant(Timber.DebugTree())
        DebugDB.getAddressLog()
        val context = InstrumentationRegistry.getTargetContext()

        ServiceJobBuilder(context, 1, 0).scheduleJob()

        // should show 3 entries in log
        for (ii in 1..50) {
            Thread.sleep(1000 * 60L)
            println("Minutes passed: $ii")
        }
    }

}