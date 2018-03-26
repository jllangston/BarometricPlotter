package com.jl.barometersensorservice

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.jl.barometerlibrary.data.BarometerDataContract
import com.jl.barometerlibrary.data.impl.BarometerDataFactoryRoom
import com.jl.barometerlibrary.rxsensor.SensorReadFactory
import io.reactivex.schedulers.Schedulers

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

        val data = sensorContract.getPressureReading()
                .subscribeOn(Schedulers.computation())
                .blockingFirst()
        dataContract.addReading(data)
    }

    private fun getDataContract(context: Context) : BarometerDataContract {
        if (dbName.isBlank()) {
            return BarometerDataFactoryRoom().getContract(context)
        } else {
            return BarometerDataFactoryRoom().getTestContract(context, dbName)
        }
    }

}