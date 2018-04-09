package com.jl.barometerlibrary.config

/**
 * Contract defining configurations
 *
 * Created by jl on 4/6/18.
 */
interface ConfigurationContract {
    fun saveAllChanges()

    fun isReschedule(): Boolean
    fun setDoReshedule(doReschedule: Boolean)

    fun getPeriod(): Int
    fun setPeriod(period: Int)

}