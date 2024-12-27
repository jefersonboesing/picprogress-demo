package com.picprogress.util

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

fun Month.name(style: TextStyle = TextStyle.FULL) = getDisplayName(style, Locale.getDefault())
    .filter { it.isLetterOrDigit() }
    .replaceFirstChar { it.uppercase() }

fun DayOfWeek.name(style: TextStyle = TextStyle.FULL) = getDisplayName(style, Locale.getDefault())
    .replaceFirstChar { it.uppercase() }

fun LocalDate.format(pattern: String = "MMM dd, yyyy"): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.toJavaLocalDate().format(formatter).replaceFirstChar { it.uppercase() }
}