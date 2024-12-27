package com.picprogress.util

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until

fun LocalDate.getWeekStartDate(): LocalDate {
    return minus(dayOfWeek.ordinal, DateTimeUnit.DAY)
}

fun LocalDate.getWeekEndDate(): LocalDate {
    return getWeekStartDate().plus(6, DateTimeUnit.DAY)
}

fun currentWeekStart(): LocalDate {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    return today.getWeekStartDate()
}

fun currentWeekEnd(): LocalDate {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    return today.getWeekEndDate()
}

fun LocalDate.getWeekNumber(referenceDate: LocalDate): Int {
    val firstWeekStart = referenceDate.getWeekStartDate()
    val secondWeekEnd = getWeekEndDate()
    return firstWeekStart.until(secondWeekEnd, DateTimeUnit.WEEK).inc().coerceAtLeast(1)
}

fun LocalDate.getDayNumber(referenceDate: LocalDate): Int {
    return referenceDate.until(this, DateTimeUnit.DAY).inc().coerceAtLeast(1)
}

fun LocalDate.getMonthNumber(referenceDate: LocalDate): Int {
    return referenceDate.withDayOfMonth(1).until(this.withDayOfMonth(1), DateTimeUnit.MONTH).inc().coerceAtLeast(1)
}

fun today(): LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

fun LocalDate.isToday(): Boolean = today() == this

fun LocalDate.withDayOfMonth(dayOfMonth: Int) = LocalDate(year, monthNumber, dayOfMonth)

fun isMorning(): Boolean {
    val currentHour = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).hour
    return currentHour in 9 .. 12
}

fun isEvening(): Boolean {
    val currentHour = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).hour
    return currentHour in 18..22
}