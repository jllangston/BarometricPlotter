package com.jl.barometricplotter.plot

import com.jl.barometerlibrary.data.BarometerReading
import io.reactivex.Flowable


/**
 * Created by jl on 1/14/18.
 *
 * Plot interface
 */

interface IPlot {

    interface View {
        fun plotData(data: List<BarometerReading>)
    }

    interface Presenter {
        fun getData(): Flowable<List<BarometerReading>>
        fun getDataPoint(): BarometerReading
    }

}

enum class PlotType {
    NORMAL, NOTIFICATION_SMALL, NOTIFICATION_LARGE
}