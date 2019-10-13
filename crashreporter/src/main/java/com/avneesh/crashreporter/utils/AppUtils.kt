package com.avneesh.crashreporter.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import java.util.*


object AppUtils {
    private fun getCurrentLauncherApp(context: Context): String {
        var str = ""
        val localPackageManager = context.packageManager
        val intent = Intent("android.intent.action.MAIN")
        intent.addCategory("android.intent.category.HOME")
        try {
            val resolveInfo = localPackageManager.resolveActivity(intent,
                    PackageManager.MATCH_DEFAULT_ONLY)
            if (resolveInfo?.activityInfo != null) {
                str = resolveInfo.activityInfo.packageName
            }
        } catch (e: Exception) {
            Log.e("AppUtils", "Exception : " + e.message)
        }

        return str
    }


    fun getDeviceDetails(context: Context): String {

        return ("Device Information\n"
                + "\nDEVICE.ID : " + getDeviceId(context)
                + "\nAPP.VERSION : " + getAppVersion(context)
                + "\nLAUNCHER.APP : " + getCurrentLauncherApp(context)
                + "\nVERSION.RELEASE : " + Build.VERSION.RELEASE
                + "\nVERSION.INCREMENTAL : " + Build.VERSION.INCREMENTAL
                + "\nVERSION.SDK.NUMBER : " + Build.VERSION.SDK_INT
                + "\nBOARD : " + Build.BOARD
                + "\nBRAND : " + Build.BRAND
                + "\nDISPLAY : " + Build.DISPLAY
                + "\nHARDWARE : " + Build.HARDWARE
                + "\nHOST : " + Build.HOST
                + "\nID : " + Build.ID
                + "\nMANUFACTURER : " + Build.MANUFACTURER
                + "\nMODEL : " + Build.MODEL
                + "\nPRODUCT : " + Build.PRODUCT
                + "\nSERIAL : " + Build.SERIAL
                + "\nTIME : " + Build.TIME
                + "\nTYPE : " + Build.TYPE
                )
    }


    private fun getDeviceId(context: Context): String {
        var androidDeviceId = getAndroidDeviceId(context)
        if (androidDeviceId == null)
            androidDeviceId = UUID.randomUUID().toString()
        return androidDeviceId

    }

    private fun getAndroidDeviceId(context: Context): String? {
        val INVALID_ANDROID_ID = "9774d56d682e549c"
        val androidId = android.provider.Settings.Secure.getString(
                context.contentResolver,
                android.provider.Settings.Secure.ANDROID_ID)
        return if (androidId == null || androidId.toLowerCase() == INVALID_ANDROID_ID) {
            null
        } else androidId
    }

    private fun getAppVersion(context: Context): Int {
        try {
            val packageInfo = context.packageManager
                    .getPackageInfo(context.packageName, 0)
            return packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException("Could not get package name: $e")
        }

    }
}
