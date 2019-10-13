package com.avneesh.crashreporter.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.avneesh.crashreporter.CrashReporter
import com.avneesh.crashreporter.R
import com.avneesh.crashreporter.adapter.MainPagerAdapter
import com.avneesh.crashreporter.utils.*
import kotlinx.android.synthetic.main.crash_reporter_activity.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File

class CrashReporterActivity : AppCompatActivity() {

    private var mainPagerAdapter: MainPagerAdapter? = null
    private var selectedTabPosition = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.crash_reporter_activity)
        setSupportActionBar(toolbar)
        if (!intent.getBooleanExtra(IS_FROM_NOTIFICATION, false))
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setupViewPager(viewpager)
        tabs.setupWithViewPager(viewpager)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val titles = arrayOf(getString(R.string.crashes), getString(R.string.exceptions),getString(R.string.custom))
        mainPagerAdapter = MainPagerAdapter(supportFragmentManager, titles)
        viewPager.adapter = mainPagerAdapter

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                selectedTabPosition = position
            }
        })
        val intent = intent
        if (intent != null && !intent.getBooleanExtra(LANDING, false)) {
            selectedTabPosition = 1
        }
        viewPager.currentItem = selectedTabPosition
        viewPager.offscreenPageLimit = 3
    }


    private fun confirmDelete() {
        alert(R.string.delete_confirmation_msg, R.string.delete_log_title) {
            positiveButton(R.string.yes) {
                dismiss()
                clearCrashLog()
            }
            negativeButton(R.string.No)
        }.show()
    }

    private fun clearCrashLog() {
        doAsync {
            var crashReportPath = CrashReporter.crashReportPath
            if (crashReportPath.isNullOrEmpty())
                crashReportPath= CrashUtil.defaultPath

            val logs = File(crashReportPath).listFiles()
            for (file in logs!!) {
                FileUtils.delete(file)
            }
            uiThread {
                mainPagerAdapter!!.clearLogs()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.log_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.delete_crash_logs) {
            confirmDelete()
            true
        } else if (item.itemId == android.R.id.home) {
            finish()
            return true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

}
