package com.picprogress.model.album

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

data class TimeFrame(val start: LocalDate, val end: LocalDate)

fun TimeFrame.isCurrent(): Boolean {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    return today in start..end
}