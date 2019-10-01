package com.karntrehan.talko.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.karntrehan.talko.extensions.toPixels

class BottomMarginDecoration(private val bottomMargin: Int = 16) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        if (position + 1 == parent.adapter?.itemCount) {
            outRect.set(0, 0, 0, bottomMargin.toPixels(parent.context))
        }
    }
}