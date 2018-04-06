package com.jl.barometerlibrary.util

import android.support.test.InstrumentationRegistry
import com.jl.barometerlibrary.impl.BarometerDataTest
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import timber.log.Timber

/**
 * Test db transfer
 *
 * Created by jl on 4/5/18.
 */
class DbSdUtilTest {

    private lateinit var dbSdUtil: DbSdUtil

    @Before
    fun setup() {
        Timber.plant(Timber.DebugTree())
        val context = InstrumentationRegistry.getTargetContext()
        dbSdUtil = DbSdUtil(context)
    }

    @Test
    fun toSd() {
        val data = BarometerDataTest()
        data.setup()
        data.addReading()

        val sdFile = dbSdUtil.toSd(data.testDbName)
        assertTrue(sdFile.exists())
    }

}