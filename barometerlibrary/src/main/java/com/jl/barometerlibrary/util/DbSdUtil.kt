package com.jl.barometerlibrary.util

import android.content.Context
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * Copy sqlite db to and from sd
 *
 * Created by jl on 4/5/18.
 */

class DbSdUtil(val context: Context) {

    /**
     * To actually get to the emulated files, need to run adb as root,
     * i.e., adb root
     */
    fun toSd(dbName : String): File {
        val sdFile = context.getExternalFilesDir(null)
        val dbPath = context.getDatabasePath(dbName)
        val sdBackupPath = "${sdFile.absolutePath}/data/db/$dbName"

        val sdBackupFile = File(sdBackupPath)
        if (!dbPath.isFile) {
            Timber.i("Db name does not exist: $dbName on path ${dbPath.absolutePath}" )
            return sdBackupFile
        }

        if (sdFile.canWrite()) {
            File(sdBackupPath).parentFile.mkdirs()
            if (sdBackupFile.exists()) {
                sdBackupFile.delete()
            }
            val source = FileInputStream(dbPath).channel
            val dest = FileOutputStream(sdBackupPath).channel
            dest.transferFrom(source, 0, source.size())
            source.close()
            dest.close()
            Timber.i("Transferred from $dbPath to $sdBackupPath")
        } else {
            Timber.i("Unable to write to path $sdFile")
        }
        return sdBackupFile
    }


}