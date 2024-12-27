package com.picprogress.local.data.dataset

import com.picprogress.local.data.mapper.AlbumTableToAlbumMapper
import com.picprogress.model.album.Album
import compicprogress.AlbumQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class AlbumDataSetImpl(
    private val albumQueries: AlbumQueries,
    private val albumTableToAlbumMapper: AlbumTableToAlbumMapper,
): AlbumDataSet {

    override suspend fun insert(album: Album) = withContext(Dispatchers.Default) {
        albumQueries.insert(
            uuid = album.uuid,
            title = album.title,
            notes = album.notes,
            frequency = album.frequency,
            themeId = album.theme.id,
            createdAt = album.createdAt.toInstant(TimeZone.currentSystemDefault()).epochSeconds
        )
    }

    override suspend fun update(album: Album) = withContext(Dispatchers.Default) {
        albumQueries.update(title = album.title, notes = album.notes, themeId = album.theme.id, id = album.id)
    }

    override suspend fun getAll(): List<Album> = withContext(Dispatchers.Default) {
        return@withContext albumQueries.getAll().executeAsList().map {
            albumTableToAlbumMapper.map(it)
        }
    }

    override suspend fun getById(albumId: Long): Album? = withContext(Dispatchers.Default) {
        return@withContext albumQueries.getById(albumId).executeAsOneOrNull()?.let {
            albumTableToAlbumMapper.map(it)
        }
    }

    override suspend fun delete(album: Album) = withContext(Dispatchers.Default) {
        return@withContext albumQueries.delete(album.id)
    }

}