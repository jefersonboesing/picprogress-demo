@file:OptIn(ExperimentalLayoutApi::class)

package com.picprogress.ds.components.card

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.picprogress.R
import com.picprogress.model.album.Album
import com.picprogress.model.album.AlbumSummary
import com.picprogress.model.album.Frequency
import com.picprogress.model.album.isCurrent
import com.picprogress.util.getWeekNumber
import com.picprogress.util.name

@Composable
fun AlbumCardContentSmall(
    album: Album,
    summary: AlbumSummary
) {
    FlowRow {
        val currentFrame = summary.allFrames.first { it.isCurrent() }
        val text = when (album.frequency) {
            Frequency.DAILY -> currentFrame.start.dayOfWeek.name()
            Frequency.MONTHLY -> currentFrame.start.month.name()
            Frequency.WEEKLY -> {
                val weekNumber = currentFrame.start.getWeekNumber(album.createdAt.date)
                stringResource(id = R.string.album_card_week_label, weekNumber)
            }
        }

        FloatingTimeFrame(
            text = text,
            isCurrent = false,
            isCompleted = summary.completedFrames.contains(currentFrame),
            theme = album.theme,
            textAlignment = Alignment.TopStart
        )
    }
}