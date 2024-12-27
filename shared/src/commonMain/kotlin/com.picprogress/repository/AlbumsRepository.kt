package com.picprogress.repository

import com.picprogress.model.album.Album
import com.picprogress.model.album.AlbumWithSummary
import com.picprogress.model.photo.Photo
import com.picprogress.model.photo.PhotoPath

interface AlbumsRepository {
    suspend fun getAlbumsWithSummary(): List<AlbumWithSummary>
    suspend fun getAlbumById(albumId: Long): Album?
    suspend fun saveAlbum(album: Album)
    suspend fun getPhotoById(photoId: Long): Photo?
    suspend fun getPhotosByAlbum(album: Album): List<Photo>
    suspend fun getLastPhotoByAlbum(album: Album): Photo?
    suspend fun addPhoto(album: Album, photoPath: PhotoPath)
    suspend fun deletePhotos(photos: List<Photo>)
    suspend fun updatePhoto(photo: Photo)
    suspend fun deleteAlbum(album: Album)
}