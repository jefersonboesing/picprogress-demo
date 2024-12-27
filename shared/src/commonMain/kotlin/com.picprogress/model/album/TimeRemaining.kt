package com.picprogress.model.album

import kotlin.time.Duration

sealed class TimeRemaining {

    data class Days(val days: Long): TimeRemaining()
    data class Hours(val hours: Long): TimeRemaining()
    data class Minutes(val minutes: Long): TimeRemaining()

}

fun Duration.toTimeRemaining(): TimeRemaining? {
    return when {
        inWholeDays > 0 -> TimeRemaining.Days(inWholeDays.inc())
        inWholeHours > 0 -> TimeRemaining.Hours(inWholeHours.inc())
        inWholeMinutes > 0 -> TimeRemaining.Minutes(inWholeMinutes.inc())
        else -> null
    }
}