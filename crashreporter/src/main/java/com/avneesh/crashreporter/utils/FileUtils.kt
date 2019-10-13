package com.avneesh.crashreporter.utils

import android.text.TextUtils

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException


object FileUtils {

    fun delete(absPath: String): Boolean {
        if (TextUtils.isEmpty(absPath)) {
            return false
        }

        val file = File(absPath)
        return delete(file)
    }

    fun delete(file: File): Boolean {
        if (!exists(file)) {
            return true
        }

        if (file.isFile) {
            return file.delete()
        }

        var result = true
        val files = file.listFiles() ?: return false
        for (index in files.indices) {
            result = result or delete(files[index])
        }
        result = result or file.delete()

        return result
    }

    fun exists(file: File?): Boolean {
        return file != null && file.exists()
    }

    fun cleanPath(absPath: String): String? {
        var absPath = absPath
        if (TextUtils.isEmpty(absPath)) {
            return absPath
        }
        try {
            val file = File(absPath)
            absPath = file.canonicalPath
        } catch (e: Exception) {

        }

        return absPath
    }

    fun getParent(file: File?): String? {
        return file?.parent
    }

    fun getParent(absPath: String?): String? {
        var absPath = absPath
        if (TextUtils.isEmpty(absPath)) {
            return null
        }
        absPath = cleanPath(absPath!!)
        val file = File(absPath!!)
        return getParent(file)
    }

    fun deleteFiles(directoryPath: String): Boolean {
        val directoryToDelete: String
        if (!TextUtils.isEmpty(directoryPath)) {
            directoryToDelete = directoryPath
        } else {
            directoryToDelete = CrashUtil.defaultPath
        }

        return delete(directoryToDelete)
    }



    fun readFromFile(file: File): String {
        val crash = StringBuilder()
        try {
            val reader = BufferedReader(FileReader(file))
            reader.forEachLine{line->
                crash.append(line)
                crash.append('\n')
            }

            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return crash.toString()
    }
}//this class is not publicly instantiable
