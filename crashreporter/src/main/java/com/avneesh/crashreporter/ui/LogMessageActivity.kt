package com.avneesh.crashreporter.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.avneesh.crashreporter.R
import com.avneesh.crashreporter.utils.AppUtils
import com.avneesh.crashreporter.utils.FileUtils
import com.avneesh.crashreporter.utils.getUriFromFile
import kotlinx.android.synthetic.main.activity_log_message.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.alert
import java.io.File

class LogMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_message)

        val intent = intent
        if (intent != null) {
            val dirPath = intent.getStringExtra("LogMessage")
            val file = File(dirPath!!)
            val crashLog = FileUtils.readFromFile(file)
            logMessage.text = crashLog
        }
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        getAppInfo()
    }

    private fun getAppInfo() {
        appInfo!!.text = AppUtils.getDeviceDetails(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.crash_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = intent
        var filePath: String? = null
        if (intent != null) {
            filePath = intent.getStringExtra("LogMessage")
        }

        if (item.itemId == R.id.delete_log) {
            confirmDelete()
            return true
        } else if (item.itemId == R.id.share_crash_log) {
            shareCrashReport(filePath)
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    private fun confirmDelete() {
        alert(R.string.delete_confirmation_msg, R.string.delete_log_title) {
            positiveButton(R.string.yes) {
                dismiss()
                var filePath: String? = null
                if (intent != null) {
                    filePath = intent.getStringExtra("LogMessage")
                }
                if (FileUtils.delete(filePath!!)) {
                    finish()
                }
            }
            negativeButton(R.string.No)
        }.show()
    }

    private fun shareCrashReport(filePath: String?) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_TEXT, appInfo!!.text.toString())
        intent.putExtra(Intent.EXTRA_STREAM, getUriFromFile(File(filePath!!)))
        startActivity(Intent.createChooser(intent, "Share via"))
    }
}
