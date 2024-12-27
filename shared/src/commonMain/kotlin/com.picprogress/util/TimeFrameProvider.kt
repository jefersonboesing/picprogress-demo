package com.picprogress.util

import com.picprogress.model.album.TimeFrame
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

interface TimeFrameProvider {

    fun getCurrentWeekDaysTimeFrame(): List<TimeFrame>

    fun getWeeksTimeFrame(minDate: LocalDate = currentWeekStart()): List<TimeFrame>

    fun getCurrentMonthsTimeFrame(year: Int = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year): List<TimeFrame>
}
