@file:OptIn(ExperimentalSerializationApi::class)

package com.picprogress.model.album

import com.picprogress.util.uuid
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class Album(
    @JsonNames("id")
    val id: Long = 0,
    @JsonNames("uuid")
    val uuid: String = uuid(),
    @JsonNames("title")
    val title: String = "",
    @JsonNames("notes")
    val notes: String = "",
    @JsonNames("frequency")
    val frequency: Frequency = Frequency.WEEKLY,
    @JsonNames("theme")
    val theme: AlbumTheme = AlbumTheme.Lavender,
    @JsonNames("createdAt")
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
)