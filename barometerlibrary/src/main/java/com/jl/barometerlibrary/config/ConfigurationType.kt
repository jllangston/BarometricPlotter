package com.jl.barometerlibrary.config

/**
 * Keys for configuration
 *
 * Created by jl on 4/6/18.
 */
enum class ConfigurationType(val key: String, val defaultValue: String) {
    DO_RESCHEDULE("do_reschedule", true.toString()),
    PERIOD("period", "15"),



}

class ConfigurationConst {
    companion object {
        const val JOB_ID = 31415
    }
}