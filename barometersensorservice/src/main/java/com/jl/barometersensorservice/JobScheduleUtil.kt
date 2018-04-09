package com.jl.barometersensorservice

import android.app.job.JobScheduler
import android.content.Context
import com.jl.barometerlibrary.config.ConfigurationConst

/**
 * Utils to check on status of job
 *
 * Created by jl on 4/7/18.
 */
class JobScheduleUtil(private val context: Context) {

    private val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

    fun isJobScheduled(jobId: Int = ConfigurationConst.JOB_ID): Boolean {
        return jobScheduler.getPendingJob(jobId) != null
    }

    fun updateJobPeriod(period: Int, jobId: Int = ConfigurationConst.JOB_ID) {
        jobScheduler.cancel(jobId)
        ServiceJobBuilder(context, period)
    }

    fun getJobPeriodMin(jobId: Int = ConfigurationConst.JOB_ID): Long {
        val period = jobScheduler.getPendingJob(jobId)?.intervalMillis ?: 15 * 60 * 1000L
        return period / 60 / 1000
    }

}