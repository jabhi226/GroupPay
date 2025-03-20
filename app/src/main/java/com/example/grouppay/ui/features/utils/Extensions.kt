package com.example.grouppay.ui.features.utils

import android.content.Context
import android.widget.Toast
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Double.formatToTwoDecimalPlaces(): String {
    val formattedString = String.format(Locale.US, "%.2f", this)
    val value = if (formattedString.contains(".")) {
        formattedString.replace(Regex("0*$"), "").replace(Regex("\\.$"), "")
    } else {
        formattedString
    }
    return value
}

fun Double.roundToTwoDecimal(): Double {
    return BigDecimal(this).setScale(2, RoundingMode.HALF_EVEN).toDouble()
}

fun String.formatToTwoDecimalPlaces(): String {
    val parsedValue = this.toDoubleOrNull() ?: return "0"
    val formattedString = String.format(Locale.US, "%.2f", parsedValue)
    return if (formattedString.contains(".")) {
        formattedString.replace(Regex("0*$"), "").replace(Regex("\\.$"), "")
    } else {
        formattedString
    }
}

fun Long.getDateInStringFormat(): String {
    return SimpleDateFormat("dd MMM yy", Locale.US).format(Date(this))
}

fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun String.getInitials(): String {
    val initials = this.trim().split(" ")
        .joinToString("") { it[0].uppercase() }

    return initials
}