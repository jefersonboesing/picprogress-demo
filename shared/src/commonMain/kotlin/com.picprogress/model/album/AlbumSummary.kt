package com.picprogress.model.album

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class AlbumSummary(
    val allFrames: List<TimeFrame> = listOf(),
    val completedFrames: List<TimeFrame> = listOf(),
    val albumSize: Int = -1,
    val timeRemaining: TimeRemaining? = null,
    val firstPhotoDate: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val isUpdated: Boolean = false
)