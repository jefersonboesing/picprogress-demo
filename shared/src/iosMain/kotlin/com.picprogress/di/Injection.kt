package com.picprogress.di

import com.picprogress.model.album.Album
import com.picprogress.model.album.AlbumTheme
import com.picprogress.model.album.Frequency
import com.picprogress.model.photo.ComparePhotos
import com.picprogress.model.photo.Photo
import com.picprogress.model.photo.PhotoPath
import com.picprogress.usecase.SaveAlbumUseCase
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
import com.picprogress.viewmodel.args.CompareArgs
import com.picprogress.viewmodel.args.PhotoSelectionArgs
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

object Injection: KoinComponent {

    fun initialize() {
        initSharedKoin { }
    }

    val saveAlbumUseCase: SaveAlbumUseCase
        get() = get()

    val homeViewModel: HomeViewModel
        get() = get()

    fun albumConfigMainViewModel(album: Album?): AlbumConfigMainViewModel {
        return get(parameters = { parametersOf(album) })
    }

    fun albumConfigThemeViewModel(theme: AlbumTheme): AlbumConfigThemeViewModel {
        return get(parameters = { parametersOf(theme) })
    }

    fun albumConfigFrequencyViewModel(frequency: Frequency): AlbumConfigFrequencyViewModel {
        return get(parameters = { parametersOf(frequency) })
    }

    fun albumViewModel(album: Album): AlbumViewModel {
        return get(parameters = { parametersOf(album) })
    }

    fun photoViewModel(album: Album, photo: Photo): PhotoViewModel {
        return get(parameters = { parametersOf(album, photo) })
    }

    fun cameraViewModel(album: Album): CameraViewModel {
        return get(parameters = { parametersOf(album) })
    }

    fun previewViewModel(photoPath: PhotoPath, album: Album): PreviewViewModel {
        return get(parameters = { parametersOf(photoPath, album) })
    }

    fun photoSelectionViewModel(album: Album, initialSelection: List<Photo>, unavailablePhotos: List<Photo>, minRequired: Int): PhotoSelectionViewModel {
        val args = PhotoSelectionArgs(
            album = album,
            initialSelection = initialSelection,
            unavailablePhotos = unavailablePhotos,
            minRequired = minRequired
        )
        return get(parameters = { parametersOf(args) })
    }

    fun compareViewModel(album: Album, comparePhotos: ComparePhotos?): CompareViewModel {
        val args = CompareArgs(
            album = album,
            comparePhotos = comparePhotos
        )
        return get(parameters = { parametersOf(args) })
    }

    fun photoConfigViewModel(photo: Photo): PhotoConfigViewModel {
        return get(parameters = { parametersOf(photo) })
    }
}