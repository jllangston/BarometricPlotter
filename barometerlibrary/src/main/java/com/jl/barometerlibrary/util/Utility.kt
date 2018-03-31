package com.jl.barometerlibrary.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Provides utility functions
 *
 * Created by jl on 3/31/18.
 */

class Utility {

    fun dateFromMs(ms : Long) = SimpleDateFormat("MM/dd hh:mm",
            Locale.getDefault()).format(Date(ms))

    fun currentTimeAsDate() = dateFromMs(System.currentTimeMillis())

}