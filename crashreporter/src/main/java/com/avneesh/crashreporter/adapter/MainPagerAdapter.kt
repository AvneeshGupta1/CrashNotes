package com.avneesh.crashreporter.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.avneesh.crashreporter.fragments.CrashLogFragment

import com.avneesh.crashreporter.utils.CRASH_SUFFIX
import com.avneesh.crashreporter.utils.CUSTOM_LOGS_SUFFIX
import com.avneesh.crashreporter.utils.EXCEPTION_SUFFIX


class MainPagerAdapter(fm: FragmentManager, private val titles: Array<String>) : FragmentPagerAdapter(fm) {

    private var crashLogFragment: CrashLogFragment? = null
    private var exceptionLogFragment: CrashLogFragment? = null
    private var customLogFragment: CrashLogFragment? = null

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                crashLogFragment = CrashLogFragment().newInstance(CRASH_SUFFIX)
                crashLogFragment!!
            }
            1 -> {
                exceptionLogFragment = CrashLogFragment().newInstance(EXCEPTION_SUFFIX)
                exceptionLogFragment!!
            }
            else -> {
                customLogFragment = CrashLogFragment().newInstance(CUSTOM_LOGS_SUFFIX)
                customLogFragment!!
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }

    fun clearLogs() {
        crashLogFragment?.clearLog()
        exceptionLogFragment?.clearLog()
        customLogFragment?.clearLog()
    }
}