package com.jl.barometersensorservice

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.os.AsyncTask
import com.jl.barometerlibrary.data.BarometerDataContract
import com.jl.barometerlibrary.data.impl.BarometerDataFactoryRoom
import com.jl.barometerlibrary.rxsensor.SensorReadContract
import com.jl.barometerlibrary.rxsensor.SensorReadFactory
import com.jl.barometerlibrary.util.Utility
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.lang.ref.WeakReference

/**
 * Creates the job to be run on some interval
 *
 * Created by jl on 3/31/18.
 */
class ReadingJobService : JobService() {

    override fun onStopJob(params: JobParameters?): Boolean {
        Timber.i("Stop job")
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Timber.d("Starting job...")
        val dataContract = getDataContract(applicationContext)
        val sensorContract = SensorReadFactory().getContract(applicationContext)
        GetReadingTask(dataContract, sensorContract, WeakReference(this), params)
                .execute()
        return true
    }

    private fun getDataContract(context: Context, dbName: String = ""):
            BarometerDataContract {
        return if (dbName.isBlank()) {
            BarometerDataFactoryRoom().getContract(context)
        } else {
            BarometerDataFactoryRoom().getTestContract(context, dbName)
        }
    }

    internal open class GetReadingTask(
            private val dataContract: BarometerDataContract,
            private val sensorContract: SensorReadContract,
            private val jobServiceRef: WeakReference<JobService>,
            private val params: JobParameters?) :
            AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg params: Void): Boolean {
            Timber.i("Retrieving reading at ${Utility().currentTimeAsDate()}")
            val data = sensorContract.getPressureReading()
                    .subscribeOn(Schedulers.computation())
                    .blockingFirst()
            dataContract.addReading(data)
            return true
        }

        override fun onPostExecute(result: Boolean?) {
            val bResult = result ?: return
            Timber.i("Finishing job")
            jobServiceRef.get()?.jobFinished(params, !bResult)
        }

    }

}


