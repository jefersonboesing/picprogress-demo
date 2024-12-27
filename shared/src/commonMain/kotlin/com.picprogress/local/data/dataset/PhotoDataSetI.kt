package com.picprogress.local.data.dataset

import com.picprogress.model.album.Album
import com.picprogress.model.album.TimeFrame
import com.picprogress.model.photo.Photo

interface PhotoDataSet {

    suspend fun insert(photo: Photo, album: Album)

    suspend fun update(photo: Photo)

    suspend fun getById(photoId: Long): Photo?

    suspend fun getLastOne(album: Album): Photo?

    suspend fun getAll(album: Album): List<Photo>

    suspend fun delete(photos: List<Photo>)

    suspend fun deleteAll()

    suspend fun getCountByAlbum(album: Album): Int

    suspend fun getCountByTimeFrame(album: Album, timeFrame: TimeFrame): Int
}
