package com.avneesh.crashreporter.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import com.avneesh.crashreporter.CrashReporter
import com.avneesh.crashreporter.R
import com.avneesh.crashreporter.adapter.CrashLogAdapter
import com.avneesh.crashreporter.ui.CustomLogMessageActivity
import com.avneesh.crashreporter.ui.LogMessageActivity
import com.avneesh.crashreporter.utils.*
import kotlinx.android.synthetic.main.crash_log.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File


class CrashLogFragment : Fragment() {

    private lateinit var mAdapter: CrashLogAdapter
    private var logList = arrayListOf<LogDetail>()

    fun newInstance(suffix: String): CrashLogFragment {
        val f = CrashLogFragment()
        val args = Bundle()
        args.putString("SUFFIX", suffix)
        f.arguments = args
        return f
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.crash_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeRecyclerView
    }

    private val homeRecyclerView by lazy {
        crashRecyclerView.itemAnimator = DefaultItemAnimator()
        crashRecyclerView.addItemDecoration(DrawerDividerItemDecoration(resources, R.drawable.line_divider))
        mAdapter = CrashLogAdapter(logList) { item -> onItemClick(item) }
        crashRecyclerView.adapter = mAdapter;
    }

    private fun loadLogs() {
        doAsync {
            try {
                var directoryPath = CrashReporter.crashReportPath

                if (directoryPath.isNullOrEmpty()) {
                    directoryPath = CrashUtil.defaultPath
                }
                val directory = File(directoryPath)
                if (!directory.exists() || !directory.isDirectory) {
                    throw RuntimeException("The path provided doesn't exists : $directoryPath")
                }

                val suffix = arguments!!.getString("SUFFIX", EXCEPTION_SUFFIX)

                val listOfFiles = directory.listFiles().filter {
                    it.name.contains(suffix)
                }.reversed()

                logList.clear()
                listOfFiles.forEach { file ->
                    logList.add(LogDetail(file.name.logTime(), file.absolutePath.readFirstLineFromFile(), file.absolutePath))
                }
                uiThread {
                    mAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.d("", "")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadLogs()
    }

    private fun onItemClick(detail: LogDetail) {
        var intent = Intent(context, LogMessageActivity::class.java)
        val suffix = arguments!!.getString("SUFFIX", EXCEPTION_SUFFIX)
        if (suffix == CUSTOM_LOGS_SUFFIX) {
            intent = Intent(context, CustomLogMessageActivity::class.java)
        }
        intent.putExtra("LogMessage", detail.filePath)
        startActivity(intent)
    }

    fun clearLog() {
        loadLogs()
    }

}
