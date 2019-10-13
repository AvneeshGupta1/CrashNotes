package com.avneesh.crashreporter.utils

import android.app.Activity
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException


fun Activity.getUriFromFile(file: File): Uri {
    if (Build.VERSION.SDK_INT < 24) {
        return Uri.fromFile(file)
    } else {
        return FileProvider.getUriForFile(
                this,
                "$packageName.provider",
                file
        )
    }
}

fun String.logTime(): String {
    return this.replace("[a-zA-Z_.]".toRegex(), "")
}

fun String.readFirstLineFromFile(): String {
    return readFirstLineFromFile(File(this))
}

fun readFirstLineFromFile(file: File): String {
    var line = ""
    try {
        val reader = BufferedReader(FileReader(file))
        line = reader.readLine()
        reader.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return line
}



