package com.avneesh.crashreporter.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.avneesh.crashreporter.CrashReporter
import com.avneesh.crashreporter.ui.CrashReporterActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    internal var context: Context? = null
    internal var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.nullPointer).setOnClickListener {
            context = null
            context!!.resources
        }

        indexOutOfBound.setOnClickListener {
            val list = ArrayList<String>()
            list.add("hello")
            val crashMe = list.get(2)
        }

        findViewById<View>(R.id.classCastExeption).setOnClickListener {
            val x = 0
            val y = x as String
        }

        customException.setOnClickListener {
            CrashReporter.logCustomLogs("This is custom logs")
        }


        //Crashes and exceptions are also captured from other threads
        Thread(Runnable {
            try {
                context = null
                context!!.resources
            } catch (e: Exception) {
                //log caught Exception
                CrashReporter.logException(e)
            }
        }).start()

        mContext = this
        findViewById<View>(R.id.crashLogActivity).setOnClickListener {
            val intent = Intent(mContext, CrashReporterActivity::class.java)
            startActivity(intent)
        }

    }
}
