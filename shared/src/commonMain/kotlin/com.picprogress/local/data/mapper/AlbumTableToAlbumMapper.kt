package com.picprogress.local.data.mapper

import com.picprogress.model.album.Album
import com.picprogress.model.album.AlbumTheme
import com.picprogress.model.album.toAlbumTheme
import compicprogress.AlbumTable
import compicprogress.PhotoQueries
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class AlbumTableToAlbumMapper(
    private val photoQueries: PhotoQueries,
) : Mapper<AlbumTable, Album> {
    override fun map(input: AlbumTable): Album {
        val firstPhoto = photoQueries.getFirst(input.id).executeAsOneOrNull()
        val createdAtInMillis = (firstPhoto?.createdAt ?: input.createdAt) * 1000
        return Album(
            id = input.id,
            uuid = input.uuid,
            title = input.title,
            notes = input.notes,
            frequency = input.frequency,
            theme = input.themeId.toAlbumTheme() ?: AlbumTheme.Plum,
            createdAt = Instant.fromEpochMilliseconds(createdAtInMillis)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        )
    }
}