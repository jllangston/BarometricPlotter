package com.jl.barometersensorservice

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.jl.barometerlibrary.config.ConfigurationFactory
import timber.log.Timber

class StartServiceActivity : AppCompatActivity() {

    private lateinit var appData: AppData

    private lateinit var periodToPosMap : Map<Int, Int>
    private lateinit var posToPeriodMap: Map<Int, Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.plant(Timber.DebugTree())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_service)
        appData = AppData(this)
        periodToPosMap = mapOf(15 to 0, 30 to 1, 60 to 2)
        posToPeriodMap = mapOf(0 to 15, 1 to 30, 2 to 60)

        setSpinnerElems()
        setButtonVisibility()

        appData.startButton.setOnClickListener { start() }
        appData.stopButton.setOnClickListener { stop() }
        appData.periodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                changePeriod(periodToPosMap[15])
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                changePeriod(posToPeriodMap[position])
            }

        }
        setSpinnerItem()
    }

    private fun setButtonVisibility() {
        val isJobScheduled = JobScheduleUtil(this).isJobScheduled()
        appData.startButton.isEnabled = !isJobScheduled
        appData.stopButton.isEnabled = isJobScheduled
    }

    private fun setSpinnerItem() {
        val period = JobScheduleUtil(this).getJobPeriodMin().toInt()
        val pos = periodToPosMap[period] ?: -1
        appData.periodSpinner.setSelection(pos)
    }

    private fun setSpinnerElems() {
        val arrayAdapter = ArrayAdapter<Int>(this,
                android.R.layout.simple_spinner_dropdown_item)
        periodToPosMap.forEach { period, pos ->  arrayAdapter.add(period)}
        appData.periodSpinner.adapter = arrayAdapter
    }

    private fun start() {
        val period = appData.configContract.getPeriod()
        ServiceJobBuilder(this, period).scheduleJob()
        setButtonVisibility()
    }

    private fun stop() {
        appData.configContract.setDoReshedule(false)
        appData.configContract.saveAllChanges()
    }

    private fun changePeriod(pos: Int?) {
        val period = posToPeriodMap[pos] ?: 15
        JobScheduleUtil(this).updateJobPeriod(period)
    }

}

internal class AppData(activity: AppCompatActivity) {
    val startButton: Button = activity.findViewById(R.id.buttonStart)
    val stopButton: Button = activity.findViewById(R.id.buttonStop)
    val periodSpinner: Spinner = activity.findViewById(R.id.spinnerPeriod)
    val configContract = ConfigurationFactory.getConfigurationContract(activity)
}
