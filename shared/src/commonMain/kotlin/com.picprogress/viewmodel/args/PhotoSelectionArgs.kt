package com.picprogress.viewmodel.args

import com.picprogress.model.album.Album
import com.picprogress.model.photo.Photo
import kotlinx.serialization.Serializable

@Serializable
data class PhotoSelectionArgs(
    val album: Album,
    val initialSelection: List<Photo>,
    val unavailablePhotos: List<Photo>,
    val minRequired: Int,
)