package com.jl.barometersensorservice

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.jl.barometerlibrary.data.BarometerDataContract
import com.jl.barometerlibrary.data.impl.BarometerDataFactoryRoom
import com.jl.barometerlibrary.rxsensor.SensorReadFactory
import com.jl.barometerlibrary.util.Utility
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by jl on 3/24/18.
 *
 * Simple service to write sensor readings to db
 */

class GetReadingService : IntentService(GetReadingService::class.toString()) {

    var dbName = ""

    override fun onHandleIntent(intent: Intent?) {
        val context = this.applicationContext
        val dataContract = getDataContract(context)

        val sensorContract = SensorReadFactory().getContract(context)


        Timber.i("Retrieving reading at ${Utility().currentTimeAsDate()}")
        val data = sensorContract.getPressureReading()
                .subscribeOn(Schedulers.computation())
                .blockingFirst()
        dataContract.addReading(data)
    }

    private fun getDataContract(context: Context) : BarometerDataContract {
        return if (dbName.isBlank()) {
            BarometerDataFactoryRoom().getContract(context)
        } else {
            BarometerDataFactoryRoom().getTestContract(context, dbName)
        }
    }

}