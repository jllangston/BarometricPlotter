package com.jl.barometricplotter.plot.view

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.jl.barometerlibrary.data.BarometerReading
import com.jl.barometricplotter.plot.IPlot
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by jl on 1/28/18.
 *
 * Plots data
 */
class AndroidChart(private val lineChart: LineChart): IPlot.View {

    val entries = ArrayList<ComparableEntry>()

    override fun plotData(data: List<BarometerReading>) {
        data.forEach { addEntry(it) }
        doPlot()
    }

    fun addEntry(reading: BarometerReading) {
        //val x = tsFormat(reading.time)
        val x = reading.time
        entries.add(ComparableEntry(x.toFloat(), reading.reading.toFloat()))
    }

    private fun doPlot() {
        entries.sort()
        formatChart()
        val lineDataSet = LineDataSet(entries as List<Entry>?, "Pressure [mb]")
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        val lineData = LineData(lineDataSet)
        lineChart.data = lineData
        lineChart.invalidate()
    }

    private fun formatChart() {
        val xAxisFormatter = HourAxisFormatter()

        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = xAxisFormatter
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelRotationAngle = 75f
        xAxis.granularity = 10f

        val xMinMax = getXMinMax()
        xAxis.axisMinimum = xMinMax.min.toFloat()
        xAxis.axisMaximum = xMinMax.max.toFloat()
        xAxis.setLabelCount(9, true)

        val yAxis = lineChart.axisLeft
        yAxis.axisMaximum = 1020f
        yAxis.axisMinimum = 970f

        lineChart.axisRight.isEnabled = false


    }

    private fun getXMinMax(): XAxisMinMax {
        val cal = Calendar.getInstance()
        val lastDate = Date(entries.last().x.toLong())
        cal.time = lastDate
        val day = cal.get(Calendar.DAY_OF_YEAR)
        cal.set(Calendar.DAY_OF_YEAR, day-4)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        val min = cal.timeInMillis

        cal.set(Calendar.DAY_OF_YEAR, day)
        val max = cal.timeInMillis
        println("Min: ${getHour(min)}, Max: ${getHour(max)}")
        return XAxisMinMax(min, max)
    }

}

//internal data class XAxisMinMax(val min: Long, val max: Long)

fun getHour(timestamp: Long) = SimpleDateFormat("MM,dd HH:mm", Locale.ENGLISH).format(Date(timestamp))

class ComparableEntry(x: Float, y: Float): Entry(x, y), Comparable<ComparableEntry> {
    override fun compareTo(other: ComparableEntry): Int {
        return this.x.compareTo(other.x)
    }

}

class HourAxisFormatter(): IAxisValueFormatter {

    val dateFormat: DateFormat = SimpleDateFormat("MM,dd HH:mm", Locale.ENGLISH)

    /**
     * Called when a value from an axis is to be formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     *
     * @param value the value to be formatted
     * @param axis  the axis the value belongs to
     * @return
     */
    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        return getHour(value.toLong())
    }

    fun getHour(timestamp: Long) = dateFormat.format(Date(timestamp))
}