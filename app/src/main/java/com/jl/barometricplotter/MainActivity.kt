package com.jl.barometricplotter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.androidplot.xy.LineAndPointFormatter
import com.androidplot.xy.XYPlot
import com.jl.barometerlibrary.data.BarometerDataContract
import com.jl.barometerlibrary.data.BarometerReading
import com.jl.barometerlibrary.data.impl.BarometerDataFactoryRoom
import com.jl.barometricplotter.plot.IPlot
import com.jl.barometricplotter.plot.PlotType
import com.jl.barometricplotter.plot.presenter.Presenter
import com.jl.barometricplotter.plot.view.AndroidPlot
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var presenter: IPlot.Presenter
    private lateinit var dataContract: BarometerDataContract

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notif_small_big)
        dataContract = BarometerDataFactoryRoom().getContract(this)
        initPresenter()


        doPlot(initChart(R.id.notif_small, PlotType.NOTIFICATION_SMALL))
        doPlot(initChart(R.id.notif_big, PlotType.NOTIFICATION_LARGE))
    }

    fun doPlot(plotView: IPlot.View) {
        dataContract.getAllData()
                .subscribeOn(Schedulers.computation())
                .subscribe {
                    plotData(it, plotView)
                }
    }


    fun plotData(data: List<BarometerReading>, plotView: IPlot.View) {
        plotView.plotData(data)
    }

    private fun initChart(idPlot: Int, plotType: PlotType): IPlot.View {
        val graph = findViewById<View>(idPlot) as XYPlot
        graph.clear()
        graph.invalidate()
        graph.setBackgroundColor(0)
        return AndroidPlot(graph, LineAndPointFormatter(this,
                R.xml.line_point_formatter_with_labels), 3, plotType)

    }

    private fun initPresenter() {
        presenter = Presenter(dataContract)
    }

}
