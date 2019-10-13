package com.avneesh.crashreporter.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.avneesh.crashreporter.CrashReporter
import com.avneesh.crashreporter.R
import org.jetbrains.anko.doAsync
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

object CrashUtil {

    private val TAG = CrashUtil::class.java.simpleName

    private val crashLogTime: String
        get() {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            return dateFormat.format(Date())
        }

    val defaultPath: String
        get() {
            val defaultPath = (CrashReporter.context!!.getExternalFilesDir(null)!!.absolutePath
                    + File.separator + CRASH_REPORT_DIR)

            val file = File(defaultPath)
            file.mkdirs()
            return defaultPath
        }

    fun saveCrashReport(throwable: Throwable) {

        val crashReportPath = CrashReporter.crashReportPath
        val filename = crashLogTime + CRASH_SUFFIX + FILE_EXTENSION
        writeToFile(crashReportPath, filename, getStackTrace(throwable))
        showNotification(throwable.localizedMessage, true)
    }

    fun logException(exception: Exception) {
        doAsync {
            val crashReportPath = CrashReporter.crashReportPath
            val filename = crashLogTime + EXCEPTION_SUFFIX + FILE_EXTENSION
            writeToFile(crashReportPath, filename, getStackTrace(exception))
        }
    }

    fun logCustomError(log: String) {
        doAsync {
            val crashReportPath = CrashReporter.crashReportPath
            val filename = crashLogTime + CUSTOM_LOGS_SUFFIX + FILE_EXTENSION
            writeToFile(crashReportPath, filename, log)
        }
    }


    private fun writeToFile(crashReportPath: String?, filename: String, crashLog: String) {
        var crashReportPath = crashReportPath

        if (TextUtils.isEmpty(crashReportPath)) {
            crashReportPath = defaultPath
        }

        val crashDir = File(crashReportPath!!)
        if (!crashDir.exists() || !crashDir.isDirectory) {
            crashReportPath = defaultPath
            Log.e(TAG, "Path provided doesn't exists : $crashDir\nSaving crash report at : $defaultPath")
        }

        val bufferedWriter: BufferedWriter
        try {
            bufferedWriter = BufferedWriter(FileWriter(
                    crashReportPath + File.separator + filename))

            bufferedWriter.write(crashLog)
            bufferedWriter.flush()
            bufferedWriter.close()
            Log.d(TAG, "crash report saved in : $crashReportPath")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun showNotification(localisedMsg: String?, isCrash: Boolean) {

        if (CrashReporter.isNotificationEnabled) {
            val context = CrashReporter.context
            val notificationManager = context!!.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            createNotificationChannel(notificationManager, context)
            val builder = NotificationCompat.Builder(context, CHANNEL_NOTIFICATION_ID)
            builder.setSmallIcon(R.drawable.ic_warning_black_24dp)

            val intent = CrashReporter.launchIntent
            intent.putExtra(LANDING, isCrash)
            intent.putExtra(IS_FROM_NOTIFICATION,true)
            intent.action = System.currentTimeMillis().toString()

            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            builder.setContentIntent(pendingIntent)

            builder.setContentTitle(context.getString(R.string.view_crash_report))

            if (TextUtils.isEmpty(localisedMsg)) {
                builder.setContentText(context.getString(R.string.check_your_message_here))
            } else {
                builder.setContentText(localisedMsg)
            }

            builder.setAutoCancel(true)
            builder.color = ContextCompat.getColor(context, R.color.colorAccent)

            notificationManager.notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager, context: Context) {
        if (Build.VERSION.SDK_INT >= 26) {
            val name = context.getString(R.string.notification_crash_report_title)
            val description = ""
            val channel = NotificationChannel(CHANNEL_NOTIFICATION_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = description
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getStackTrace(e: Throwable): String {
        val result = StringWriter()
        val printWriter = PrintWriter(result)

        e.printStackTrace(printWriter)
        val crashLog = result.toString()
        printWriter.close()
        return crashLog
    }
}//this class is not publicly instantiable
