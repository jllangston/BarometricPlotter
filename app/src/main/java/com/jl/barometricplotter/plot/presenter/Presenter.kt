package com.jl.barometricplotter.plot.presenter

import com.jl.barometerlibrary.data.BarometerDataContract
import com.jl.barometerlibrary.data.BarometerReading
import com.jl.barometricplotter.plot.IPlot
import io.reactivex.Flowable

/**
 * Created by jl on 1/18/18.
 *
 * Presenter class
 */
class Presenter(private val dataContract: BarometerDataContract) : IPlot.Presenter {

    override fun getData(): Flowable<List<BarometerReading>> {
        return dataContract.getAllData()
    }

    override fun getDataPoint(): BarometerReading {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}