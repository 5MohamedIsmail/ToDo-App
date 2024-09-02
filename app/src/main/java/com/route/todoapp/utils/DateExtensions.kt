package com.route.todoapp.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Calendar.ignoreTime() {
    set(Calendar.HOUR, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}

fun Calendar.ignoreDate() {
    set(Calendar.DAY_OF_MONTH, 0)
    set(Calendar.MONTH, 0)
    set(Calendar.YEAR, 0)
}

fun Calendar.setTaskDate(year: Int, month: Int, dayOfMonth: Int) {
    set(Calendar.YEAR, year)
    set(Calendar.MONTH, month)
    set(Calendar.DAY_OF_MONTH, dayOfMonth)
}

fun Calendar.formatTaskDate(): String {
    val dateFormat = SimpleDateFormat("yyyy MMM dd", Locale.getDefault())
    return dateFormat.format(time)
}

fun Calendar.setTaskTime(hour: Int, minute: Int) {
    set(Calendar.HOUR, hour)
    set(Calendar.MINUTE, minute)
}

fun Calendar.formatTaskTime(): String {
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return dateFormat.format(time)
}

fun Long.formatTaskTime(): String {
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return dateFormat.format(this)
}

fun Long.formatTaskDate(): String {
    val dateFormat = SimpleDateFormat("yyyy MMM dd", Locale.getDefault())
    return dateFormat.format(this)
}