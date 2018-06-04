package com.jl.barometricplotter.plot.view

import com.androidplot.ui.Size
import com.androidplot.ui.SizeMetric
import com.androidplot.ui.SizeMode
import com.androidplot.ui.widget.Widget
import com.androidplot.xy.*
import com.jl.barometerlibrary.data.BarometerReading
import com.jl.barometricplotter.plot.IPlot
import com.jl.barometricplotter.plot.PlotType
import java.text.*
import java.util.*

class AndroidPlot(private val xyPlot: XYPlot,
                  private val linePointFormatter: LineAndPointFormatter,
                  private val nDays: Int,
                  private val plotType: PlotType):
        IPlot.View {

    override fun plotData(data: List<BarometerReading>) {
        if (data.isEmpty()) {
            xyPlot.domainTitle.text = "Empty data set!"
            return
        }
        data.sorted()
        val xVals = data.map { it.time }
        val yVals = data.map { it.reading }

        val xySeries = SimpleXYSeries(xVals, yVals, "pressure [mb]")

        formatPlot(data)

        xyPlot.addSeries(xySeries, linePointFormatter)
        xyPlot.redraw()
    }

    private fun formatPlot(data: List<BarometerReading>) {
        formatTime()
        formatPressure()
        val minMax = getXMinMax(data)
        xyPlot.setDomainBoundaries(minMax.min, minMax.max, BoundaryMode.FIXED)
        val dStepSize = (minMax.max - minMax.min) / nDays
        xyPlot.setDomainStep(StepMode.INCREMENT_BY_VAL, dStepSize.toDouble())
        xyPlot.setRangeStep(StepMode.INCREMENT_BY_VAL, 5.0)

        xyPlot.setMarkupEnabled(true)

        val lom = xyPlot.layoutManager

        var maxPressure = 1010
        var marginBottom = 40f
        var marginTop = 10f
        if (plotType == PlotType.NOTIFICATION_SMALL) {
            maxPressure = 0
            marginTop = 0f
            marginBottom = -10f
            xyPlot.graph.paddingBottom = 0f
            xyPlot.graph.rangeGridLinePaint = null
            xyPlot.graph.setLineLabelEdges(XYGraphWidget.Edge.NONE)
            xyPlot.graph.domainOriginLinePaint = null
            xyPlot.graph.rangeOriginLinePaint = null
            val sizeMetricY = SizeMetric(0f, SizeMode.FILL)
            val size = Size(sizeMetricY, SizeMetric(0f, SizeMode.FILL))

            xyPlot.graph.size = size
        } else {
            xyPlot.setRangeBoundaries(980, 1010, BoundaryMode.FIXED)
        }

        val widgets = mutableListOf<Widget>()
        lom.stream().forEach { w -> if (w is XYGraphWidget) widgets.add(w) }

        lom.clear()
        widgets.forEach { w -> lom.add(w) }
        println("n widgets: ${lom.size}")
        xyPlot.graph.marginBottom = marginBottom
        xyPlot.graph.marginTop = marginTop
        xyPlot.graph.marginLeft = 0f
        calcPaddingLeft(maxPressure)
        xyPlot.graph.lineExtensionBottom = 0f
        xyPlot.graph.lineExtensionLeft = 0f
        xyPlot.legend = null
        xyPlot.setRangeLabel(null)
        xyPlot.graph.refreshLayout()
        xyPlot
    }

    private fun formatPressure() {
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

    private fun formatTime() {
        xyPlot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM)
                .format = object: Format() {
            override fun format(obj: Any?, toAppendTo: StringBuffer?, pos: FieldPosition?): StringBuffer {
                val pattern = "MM/dd"
                return SimpleDateFormat(pattern, Locale.ENGLISH)
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

        val max = if (plotType == PlotType.NOTIFICATION_SMALL) {
            lastDate.time
        } else {
            cal.set(Calendar.DAY_OF_YEAR, day)
            cal.timeInMillis
        }
        println("Min: ${getHour(min)}, Max: ${getHour(max)}")
        return XAxisMinMax(min, max)
    }

    private fun calcPaddingLeft(xMax: Int) {
        var leftPad = 0f
        if (xMax > 0) {
            val digits = (Math.floor(Math.log10(xMax.toDouble()))).toInt() + 1
            leftPad = digits * xyPlot.graph.domainGridLinePaint.textSize * 1
        }
        println("pad left pre: ${xyPlot.graph.paddingLeft}")
        xyPlot.graph.paddingLeft = leftPad
        println("pad left post: ${xyPlot.graph.paddingLeft}")
    }

}

internal data class XAxisMinMax(val min: Long, val max: Long)