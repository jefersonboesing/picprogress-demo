package com.picprogress.viewmodel.args

import com.picprogress.model.album.Album
import com.picprogress.model.photo.ComparePhotos
import kotlinx.serialization.Serializable

@Serializable
data class CompareArgs(val album: Album, val comparePhotos: ComparePhotos?)