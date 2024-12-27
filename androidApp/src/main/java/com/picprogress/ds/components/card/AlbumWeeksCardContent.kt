@file:OptIn(ExperimentalLayoutApi::class)

package com.picprogress.ds.components.card

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.picprogress.R
import com.picprogress.model.album.Album
import com.picprogress.model.album.AlbumSummary
import com.picprogress.model.album.isCurrent
import com.picprogress.util.getWeekNumber

@Composable
fun FlowRowScope.AlbumWeeksCardContent(
    album: Album,
    summary: AlbumSummary,
) {
    summary.allFrames.forEach {
        val weekNumber = it.start.getWeekNumber(album.createdAt.date)
        FloatingTimeFrame(
            text = stringResource(id = R.string.album_card_week_label, weekNumber),
            isCurrent = it.isCurrent(),
            isCompleted = summary.completedFrames.contains(it),
            theme = album.theme
        )
    }
}