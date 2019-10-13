package com.avneesh.crashreporter.sample

import android.app.Application

import com.avneesh.crashreporter.CrashReporter

/**
 * Created by bali on 02/08/17.
 */

class CrashReporterSampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            //initialise reporter with external path
            CrashReporter.initialize(this)
        }
    }
}
