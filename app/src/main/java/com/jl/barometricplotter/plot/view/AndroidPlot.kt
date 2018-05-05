package com.jl.barometricplotter.plot.view

import com.androidplot.xy.*
import com.jl.barometerlibrary.data.BarometerReading
import com.jl.barometricplotter.plot.IPlot
import java.text.*
import java.util.*

class AndroidPlot(private val xyPlot: XYPlot,
                  private val linePointFormatter: LineAndPointFormatter,
                  private val nDays: Int):
        IPlot.View {

    override fun plotData(data: List<BarometerReading>) {
        data.sorted()
        val xVals = data.map { it.time }
        val yVals = data.map { it.reading }

        val xySeries = SimpleXYSeries(xVals, yVals, "pressure [mb]")

        formatPlot(data)
        xyPlot.addSeries(xySeries, linePointFormatter)
        xyPlot.redraw()
    }

    fun formatPlot(data: List<BarometerReading>) {
        formatTime()
        formatPressure()
        val minMax = getXMinMax(data)
        xyPlot.setDomainBoundaries(minMax.min, minMax.max, BoundaryMode.FIXED)
        xyPlot.setRangeBoundaries(980, 1010, BoundaryMode.FIXED)
        val dStepSize = (minMax.max - minMax.min) / nDays
        xyPlot.setDomainStep(StepMode.INCREMENT_BY_VAL, dStepSize.toDouble())
        xyPlot.setRangeStep(StepMode.INCREMENT_BY_VAL, 5.0)
        calcPaddingLeft(1010)

//        xyPlot.graph.setLineLabelRenderer(XYGraphWidget.Edge.RIGHT, XYGraphWidget.LineLabelRenderer())

    }

    fun calcPaddingLeft(xMax: Int) {
        val digits = (Math.floor(Math.log10(xMax.toDouble()))).toInt() - 1
        val leftpad = digits * xyPlot.graph.domainGridLinePaint.textSize / 2
        println("pad left pre: ${xyPlot.graph.paddingLeft}")
        xyPlot.graph.paddingLeft = leftpad
        println("pad left post: ${xyPlot.graph.paddingLeft}")
    }

    fun formatPressure() {
        xyPlot.graph.getLineLabelStyle(XYGraphWidget.Edge.LEFT)
                .format = object: Format() {
            override fun parseObject(source: String?, pos: ParsePosition?): Any {
                return Any()
            }

            override fun format(obj: Any?, toAppendTo: StringBuffer?, pos: FieldPosition?): StringBuffer {
                val format = DecimalFormat()
                format.isParseIntegerOnly = true
                format.groupingSize = 5
                return format.format(obj as Number, toAppendTo, pos)
            }
        }
    }

    fun formatTime() {
        xyPlot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM)
                .format = object: Format() {
            override fun format(obj: Any?, toAppendTo: StringBuffer?, pos: FieldPosition?): StringBuffer {
                return SimpleDateFormat("MM/dd", Locale.ENGLISH)
                        .format(obj as Number, toAppendTo, pos)
            }

            override fun parseObject(source: String?, pos: ParsePosition?): Any {
                return Any()
            }

        }
    }

    private fun getXMinMax(data: List<BarometerReading>): XAxisMinMax {
        val cal = Calendar.getInstance()
        val lastDate = Date(data.last().time)
        cal.time = lastDate
        val day = cal.get(Calendar.DAY_OF_YEAR)
        cal.set(Calendar.DAY_OF_YEAR, day-nDays)
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 0)
        val min = cal.timeInMillis

        cal.set(Calendar.DAY_OF_YEAR, day)
        val max = cal.timeInMillis
        println("Min: ${getHour(min)}, Max: ${getHour(max)}")
        return XAxisMinMax(min, max)
    }

}

internal data class XAxisMinMax(val min: Long, val max: Long)