package com.picprogress.model.album

sealed class AlbumNotificationType(val album: Album) {
    class DayStart(album: Album): AlbumNotificationType(album)
    class DayEnd(album: Album): AlbumNotificationType(album)

    class MonthStart(album: Album): AlbumNotificationType(album)
    class MonthEnd(album: Album): AlbumNotificationType(album)

    class WeekStart(album: Album): AlbumNotificationType(album)
    class WeekEnd(album: Album): AlbumNotificationType(album)
}