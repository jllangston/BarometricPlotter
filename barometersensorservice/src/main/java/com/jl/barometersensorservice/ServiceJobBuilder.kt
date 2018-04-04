package com.jl.barometersensorservice

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context

/**
 * Schedules service job
 *
 * Created by jl on 3/31/18.
 */
class ServiceJobBuilder(private val context: Context,
                        private val period: Int,
                        private val jobId : Int) {

    fun scheduleJob() {
        val componentName = ComponentName(context, ReadingJobService::class.java)
        val timeLength = 1000L * 60 * period
        val builder = JobInfo.Builder(jobId, componentName)
        builder.setPeriodic(timeLength)
        val scheduler = context.getSystemService(JobScheduler::class.java)
        scheduler.schedule(builder.build())
    }

}