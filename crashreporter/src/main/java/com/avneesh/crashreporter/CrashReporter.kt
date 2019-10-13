package com.avneesh.crashreporter

import android.content.Context
import android.content.Intent

import com.avneesh.crashreporter.ui.CrashReporterActivity
import com.avneesh.crashreporter.utils.CrashReporterNotInitializedException
import com.avneesh.crashreporter.utils.CrashReporterExceptionHandler
import com.avneesh.crashreporter.utils.CrashUtil

object CrashReporter {

    private var applicationContext: Context? = null

    var crashReportPath: String? = null
        private set

    var isNotificationEnabled = true
        private set

    val context: Context?
        get() {
            if (applicationContext == null) {
                try {
                    throw CrashReporterNotInitializedException("Initialize CrashReporter : call CrashReporter.initialize(context, crashReportPath)")
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            return applicationContext
        }

    val launchIntent: Intent
        get() = Intent(applicationContext, CrashReporterActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    fun initialize(context: Context) {
        applicationContext = context
        setUpExceptionHandler()
    }

    fun initialize(context: Context, crashReportSavePath: String) {
        applicationContext = context
        crashReportPath = crashReportSavePath
        setUpExceptionHandler()
    }

    private fun setUpExceptionHandler() {
        if (Thread.getDefaultUncaughtExceptionHandler() !is CrashReporterExceptionHandler) {
            Thread.setDefaultUncaughtExceptionHandler(CrashReporterExceptionHandler())
        }
    }

    //LOG Exception APIs
    fun logException(exception: Exception) {
        CrashUtil.logException(exception)
    }

    fun logCustomLogs(log: String) {
        CrashUtil.logCustomError(log)
    }

    fun disableNotification() {
        isNotificationEnabled = false
    }

}// This class in not publicly instantiable
