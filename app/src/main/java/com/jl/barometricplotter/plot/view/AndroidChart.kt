package com.jl.barometricplotter.plot.view

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.jl.barometerlibrary.data.BarometerReading
import com.jl.barometricplotter.plot.IPlot
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by jl on 1/28/18.
 *
 * Plots data
 */
class AndroidChart(private val lineChart: LineChart): IPlot.View {

    val entries = ArrayList<Entry>()

    override fun plotData(data: Flowable<BarometerReading>) {
        data.subscribeOn(Schedulers.io()).subscribe { doPlot(it) }
    }

    override fun plotData(data: List<BarometerReading>) {
        data.forEach { doPlot(it) }
    }

    override fun plotData(data: Observable<BarometerReading>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun doPlot(reading: BarometerReading) {
        val x = tsFormat(reading.time)
        entries.add(Entry(x, reading.reading.toFloat()))
        val lineDataSet = LineDataSet(entries, "Pressure [mb]")
        val lineData = LineData(lineDataSet)
        lineChart.data = lineData
        lineChart.invalidate()
    }

    private fun tsFormat(ts : Long): Float {
        val cal = Calendar.getInstance()
        cal.timeInMillis = ts
        return (
                //cal.get(Calendar.HOUR)*60*60 +
                cal.get(Calendar.MINUTE)*60 +
                cal.get(Calendar.SECOND)
                ).toFloat()
    }

}