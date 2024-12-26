package com.example.grouppay.ui.features.utils

import android.content.Context
import android.widget.Toast
import java.util.Locale

fun Double.formatToTwoDecimalPlaces(): String {
    val formattedString = String.format(Locale.US, "%.2f", this)
    return formattedString.replace(Regex("([.]0+$|(?<=\\d)0+$)"), "")
}

fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}