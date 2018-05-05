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
import com.jl.barometricplotter.plot.presenter.Presenter
import com.jl.barometricplotter.plot.view.AndroidPlot
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity(), IPlot.View {

    lateinit var presenter: IPlot.Presenter
    lateinit var graphViewPlot : IPlot.View
    lateinit var dataContract: BarometerDataContract

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dataContract = BarometerDataFactoryRoom().getContract(this)
        initChart()
        initPresenter()
        doPlot()
    }

    fun doPlot() {
        dataContract.getAllData()
                .subscribeOn(Schedulers.computation())
                .subscribe {
                    plotData(it)
                }
    }

    override fun plotData(data: List<BarometerReading>) {
        graphViewPlot.plotData(data)
    }

    private fun initChart() {
        val graph = findViewById<View>(R.id.plot) as XYPlot
        graph.clear()
        graph.invalidate()
        graph.setBackgroundColor(0)
        graphViewPlot = AndroidPlot(graph, LineAndPointFormatter(this,
                R.xml.line_point_formatter_with_labels), 3)

    }

    private fun initPresenter() {
        presenter = Presenter(dataContract)
    }

}
