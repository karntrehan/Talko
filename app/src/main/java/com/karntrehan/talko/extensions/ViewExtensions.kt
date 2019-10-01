package com.karntrehan.talko.extensions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.karntrehan.talko.R
import kotlin.random.Random

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

private val PLACEHOLDERCOLORS = intArrayOf(
    R.color.placeholder_color1,
    R.color.placeholder_color2,
    R.color.placeholder_color3,
    R.color.placeholder_color4
)

fun randomColor(): Int {
    return PLACEHOLDERCOLORS[Random.nextInt(PLACEHOLDERCOLORS.size)]
}

fun Int.toPixels(context: Context): Int {
    return (this * context.resources.displayMetrics.density).toInt()
}