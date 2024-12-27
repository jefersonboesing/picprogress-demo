package com.picprogress.util

import com.picprogress.model.album.TimeFrame
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.minus
import kotlinx.datetime.plus

class TimeFrameProviderImpl : TimeFrameProvider {

    override fun getCurrentWeekDaysTimeFrame(): List<TimeFrame> {
        val currentWeekStart = currentWeekStart()
        return DayOfWeek.values().map {
            val date = currentWeekStart.plus(it.ordinal, DateTimeUnit.DAY)
            TimeFrame(start = date, end = date)
        }
    }

    override fun getWeeksTimeFrame(minDate: LocalDate): List<TimeFrame> {
        val currentWeekStart = currentWeekStart()
        var offset = ((minDate.getWeekStartDate().toEpochDays() - currentWeekStart.toEpochDays()) / 7).coerceIn(minimumValue = -2, maximumValue = 0)
        return (1..4).map {
            val startWeek = currentWeekStart.plus(DatePeriod(days = 7 * offset++))
            val endWeek = startWeek.getWeekEndDate()
            TimeFrame(start = startWeek, end = endWeek)
        }
    }

    override fun getCurrentMonthsTimeFrame(year: Int): List<TimeFrame> {
        return Month.values().map { month ->
            val start = LocalDate(year, month, 1)
            val end = start.plus(DatePeriod(months = 1)).minus(DatePeriod(days = 1))
            TimeFrame(start = start, end = end)
        }
    }

}