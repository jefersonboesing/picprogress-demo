package com.picprogress.local.data.dataset

import com.picprogress.model.album.Album

interface AlbumDataSet {

    suspend fun insert(album: Album)

    suspend fun update(album: Album)

    suspend fun getAll(): List<Album>

    suspend fun getById(albumId: Long): Album?

    suspend fun delete(album: Album)
}
