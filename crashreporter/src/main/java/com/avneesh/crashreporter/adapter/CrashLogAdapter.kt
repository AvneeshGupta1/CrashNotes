package com.avneesh.crashreporter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.avneesh.crashreporter.R
import com.avneesh.crashreporter.utils.LogDetail
import kotlinx.android.synthetic.main.custom_item.view.*
import java.util.*


class CrashLogAdapter(private var crashFileList: ArrayList<LogDetail>, private val itemClick: (LogDetail) -> Unit) : RecyclerView.Adapter<CrashLogAdapter.CrashLogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrashLogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_item, null)
        return CrashLogViewHolder(view)
    }

    override fun onBindViewHolder(holder: CrashLogViewHolder, position: Int) {
        val item = crashFileList[position]
        holder.item.messageLogTime.text = item.logTime
        holder.item.textViewMsg.text = item.message
        holder.itemView.setOnClickListener { itemClick(item) }
    }

    override fun getItemCount(): Int {
        return crashFileList.size
    }

    class CrashLogViewHolder(val item: View) : RecyclerView.ViewHolder(item)
}
