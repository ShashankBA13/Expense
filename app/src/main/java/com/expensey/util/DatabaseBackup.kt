package com.expensey.util

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object DatabaseBackup {

    fun backupDatabase(context: Context, dbName: String) {
        try {
            // Create a backup folder if it doesn't exist
            val backupDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "BackupFolder")
            if (!backupDir.exists()) {
                backupDir.mkdirs()
            }

            // Create a backup file path with a timestamp
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val backupDBPath = File(backupDir, "backup_$timeStamp.db")

            // Get the current database file
            val currentDBPath = context.getDatabasePath(dbName).absolutePath
            val currentDBFile = File(currentDBPath)

            // Copy the current database file to the backup file
            copyFile(currentDBFile, backupDBPath)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun copyFile(src: File, dst: File) {
        FileInputStream(src).channel.use { input ->
            FileOutputStream(dst).channel.use { output ->
                output.transferFrom(input, 0, input.size())
            }
        }
    }
}
