package com.example.grouppay.ui.features.utils

import java.util.Locale

fun Double.formatToTwoDecimalPlaces(): String {
    val formattedString = String.format(Locale.US, "%.2f", this)
    return formattedString.replace(Regex("([.]0+$|(?<=\\d)0+$)"), "")
}
