package com.picprogress.viewmodel.di

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
import org.koin.dsl.module

val platformViewModelModule = module {
    factory { HomeViewModel(get()) }
    factory { params -> AlbumConfigMainViewModel(params[0], get(), get()) }
    factory { params -> AlbumConfigThemeViewModel(params[0]) }
    factory { params -> AlbumConfigFrequencyViewModel(params[0]) }
    factory { params -> AlbumViewModel(params[0], get(), get(), get(), get(), get()) }
    factory { params -> PhotoViewModel(params[0], params[1], get(), get()) }
    factory { params -> PhotoConfigViewModel(params[0], get()) }
    factory { params -> CameraViewModel(params[0], get()) }
    factory { params -> PreviewViewModel(params[0], params[1], get(), get()) }
    factory { params -> PhotoSelectionViewModel(params[0], get()) }
    factory { params -> CompareViewModel(params[0], get(), get()) }
}