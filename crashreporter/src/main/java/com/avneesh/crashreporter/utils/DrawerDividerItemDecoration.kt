package com.avneesh.crashreporter.utils

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView


class DrawerDividerItemDecoration(resources: Resources, drawableId: Int) : RecyclerView.ItemDecoration() {
    private val mDivider: Drawable = resources.getDrawable(drawableId)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as androidx.recyclerview.widget.RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider.intrinsicHeight
            mDivider.setBounds(PADDING, top, right, bottom)
            mDivider.draw(c)
        }
    }

    companion object {
        private val PADDING = 0
    }
}