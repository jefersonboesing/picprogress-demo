@file:OptIn(ExperimentalSerializationApi::class)

package com.picprogress.model.photo

import com.picprogress.model.album.Album
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class AlbumPhoto(
    @JsonNames("album")
    val album: Album,
    @JsonNames("photoPath")
    val photoPath: PhotoPath
)