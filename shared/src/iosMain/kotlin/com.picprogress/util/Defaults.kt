package com.picprogress.util

import com.picprogress.model.album.Album
import com.picprogress.model.album.AlbumSummary
import com.picprogress.model.photo.Photo
import com.picprogress.model.photo.PhotoPath
import com.picprogress.viewmodel.AlbumConfigFrequencyViewModel
import com.picprogress.viewmodel.AlbumConfigMainViewModel
import com.picprogress.viewmodel.AlbumConfigThemeViewModel
import com.picprogress.viewmodel.AlbumViewModel
import com.picprogress.viewmodel.CameraViewModel
import com.picprogress.viewmodel.CompareViewModel
import com.picprogress.viewmodel.HomeViewModel
import com.picprogress.viewmodel.PhotoConfigViewModel
import com.picprogress.viewmodel.PhotoSelectionViewModel
import com.picprogress.viewmodel.PhotoViewModel
import com.picprogress.viewmodel.PreviewViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object Defaults {

    // Models
    fun album() = Album()
    fun photo(photoPath: PhotoPath = PhotoPath("")) = Photo(
        uuid = uuid(),
        photoPath = photoPath,
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )
    fun albumSummary() = AlbumSummary()

    // Album Config
    fun stateAlbumConfigTheme() = AlbumConfigThemeViewModel.State()
    fun stateAlbumConfigFrequency() = AlbumConfigFrequencyViewModel.State()
    fun stateAlbumConfigMain() = AlbumConfigMainViewModel.State(album())

    // Home
    fun stateHome() = HomeViewModel.State()

    // Album
    fun stateAlbum() = AlbumViewModel.State()

    // Photo
    fun statePhoto() = PhotoViewModel.State(album(), photo())

    // Camera
    fun stateCamera() = CameraViewModel.State()

    // Camera
    fun statePreview() = PreviewViewModel.State(PhotoPath(""))

    // PhotoSelection
    fun statePhotoSelection() = PhotoSelectionViewModel.State()

    // PhotoConfig
    fun statePhotoConfig() = PhotoConfigViewModel.State(today(), today())

    // Compare
    fun stateCompare() = CompareViewModel.State(album(), null)
}
