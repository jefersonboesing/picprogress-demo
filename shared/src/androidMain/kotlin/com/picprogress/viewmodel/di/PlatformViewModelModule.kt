package com.picprogress.viewmodel.di

import com.picprogress.viewmodel.AlbumConfigFrequencyViewModel
import com.picprogress.viewmodel.AlbumConfigMainViewModel
import com.picprogress.viewmodel.AlbumConfigThemeViewModel
import com.picprogress.viewmodel.AlbumConfigViewModel
import com.picprogress.viewmodel.AlbumViewModel
import com.picprogress.viewmodel.CameraViewModel
import com.picprogress.viewmodel.CompareViewModel
import com.picprogress.viewmodel.HomeViewModel
import com.picprogress.viewmodel.PhotoConfigViewModel
import com.picprogress.viewmodel.PhotoSelectionViewModel
import com.picprogress.viewmodel.PhotoViewModel
import com.picprogress.viewmodel.PreviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val platformViewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { AlbumConfigViewModel() }
    viewModel { params -> AlbumConfigThemeViewModel(params[0]) }
    viewModel { params -> AlbumConfigFrequencyViewModel(params[0]) }
    viewModel { params -> AlbumConfigMainViewModel(params[0], get(), get()) }
    viewModel { params -> AlbumViewModel(params[0], get(), get(), get(), get(), get()) }
    viewModel { params -> CameraViewModel(params[0], get()) }
    viewModel { params -> PreviewViewModel(params[0], params[1], get(), get()) }
    viewModel { params -> PhotoViewModel(params[0], params[1], get(), get()) }
    viewModel { params -> PhotoConfigViewModel(params[0], get()) }
    viewModel { params -> CompareViewModel(params[0], get(), get()) }
    viewModel { params -> PhotoSelectionViewModel(params[0], get()) }
}