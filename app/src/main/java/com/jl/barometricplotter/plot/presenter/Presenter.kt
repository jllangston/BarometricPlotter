package com.jl.barometricplotter.plot.presenter

import com.jl.barometerlibrary.data.BarometerReading
import com.jl.barometricplotter.plot.IPlot
import com.jl.barometerlibrary.sensor.BarometerSensorListener
import io.reactivex.Observable

/**
 * Created by jl on 1/18/18.
 *
 * Presenter class
 */
class Presenter(val sensorListener: BarometerSensorListener) : IPlot.Presenter {

    override fun getData(): Observable<BarometerReading> {
        return Observable.fromIterable(sensorListener.readings)
    }

    override fun getDataPoint(): BarometerReading {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}