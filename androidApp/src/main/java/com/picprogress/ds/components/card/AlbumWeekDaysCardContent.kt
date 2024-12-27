@file:OptIn(ExperimentalLayoutApi::class)

package com.picprogress.ds.components.card

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.runtime.Composable
import com.picprogress.model.album.Album
import com.picprogress.model.album.AlbumSummary
import com.picprogress.model.album.isCurrent
import com.picprogress.util.name
import java.time.format.TextStyle

@Composable
fun FlowRowScope.AlbumWeekDaysCardContent (
    album: Album,
    summary: AlbumSummary,
) {
    summary.allFrames.forEach {
        FloatingTimeFrame(
            text = it.start.dayOfWeek.name(TextStyle.NARROW),
            isCurrent = it.isCurrent(),
            isCompleted = summary.completedFrames.contains(it),
            theme = album.theme
        )
    }
}