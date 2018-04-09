package com.jl.barometerlibrary.config

import android.content.Context
import android.preference.PreferenceManager

/**
 * Reads configuration
 *
 * Created by jl on 4/6/18.
 */
class ConfigurationFactory {
    companion object {
        fun getConfigurationContract(context: Context): ConfigurationContract {
            return ConfigurationHelper(context)
        }
    }
}

internal class ConfigurationHelper(context: Context): ConfigurationContract {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val editor = preferences.edit()

    override fun saveAllChanges() {
        editor.apply()
    }

    override fun isReschedule(): Boolean {
        val defValue = ConfigurationType.DO_RESCHEDULE.defaultValue.toBoolean()
        return preferences.getBoolean(ConfigurationType.DO_RESCHEDULE.key, defValue)
    }

    override fun setDoReshedule(doReschedule: Boolean) {
        editor.putBoolean(ConfigurationType.DO_RESCHEDULE.key, doReschedule)
    }

    override fun getPeriod(): Int {
        val defValue = ConfigurationType.PERIOD.defaultValue.toInt()
        return preferences.getInt(ConfigurationType.PERIOD.key, defValue)
    }

    override fun setPeriod(period: Int) {
        editor.putInt(ConfigurationType.PERIOD.key, period)
    }

}
