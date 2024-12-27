@file:OptIn(ExperimentalLayoutApi::class)

package com.picprogress.ds.components.card

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.runtime.Composable
import com.picprogress.model.album.Album
import com.picprogress.model.album.AlbumSummary
import com.picprogress.util.name
import com.picprogress.util.today
import java.time.format.TextStyle

@Composable
fun FlowRowScope.AlbumMonthsCardContent(
    album: Album,
    summary: AlbumSummary,
) {
    summary.allFrames.forEach {
        val isFutureMonth = it.start.monthNumber > today().monthNumber
        CircularTimeFrame(
            text = if (isFutureMonth) "" else it.start.month.name(TextStyle.SHORT),
            isCompleted = summary.completedFrames.contains(it),
            theme = album.theme
        )
    }
}