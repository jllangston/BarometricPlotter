package com.jl.barometersensorservice

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import com.jl.barometerlibrary.config.ConfigurationConst

/**
 * Schedules service job
 *
 * Created by jl on 3/31/18.
 */
class ServiceJobBuilder(private val context: Context,
                        private val period: Int) {

    fun scheduleJob() {
        val componentName = ComponentName(context, ReadingJobService::class.java)
        val timeLength = 1000L * 60 * period
        val builder = JobInfo.Builder(ConfigurationConst.JOB_ID, componentName)
        builder.setPeriodic(timeLength)
        val scheduler = context.getSystemService(JobScheduler::class.java)
        scheduler.schedule(builder.build())
    }

}