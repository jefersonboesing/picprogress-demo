package com.picprogress.usecase.di

import com.picprogress.usecase.SaveAlbumUseCase
import com.picprogress.usecase.AddPhotoUseCase
import com.picprogress.usecase.DeleteAlbumUseCase
import com.picprogress.usecase.DeletePhotosUseCase
import com.picprogress.usecase.GetAlbumUseCase
import com.picprogress.usecase.GetAlbumsWithSummaryUseCase
import com.picprogress.usecase.GetLastPhotoByAlbumUseCase
import com.picprogress.usecase.GetPhotoUseCase
import com.picprogress.usecase.GetPhotoWithAlbumUseCase
import com.picprogress.usecase.GetPhotosByAlbumUseCase
import com.picprogress.usecase.GetPreferencesUseCase
import com.picprogress.usecase.SetCompareOverTutorialViewedUseCase
import com.picprogress.usecase.UpdatePhotoUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetAlbumsWithSummaryUseCase(get()) }
    factory { SaveAlbumUseCase(get()) }
    factory { AddPhotoUseCase(get()) }
    factory { GetPhotosByAlbumUseCase(get()) }
    factory { GetAlbumUseCase(get()) }
    factory { GetPhotoUseCase(get()) }
    factory { UpdatePhotoUseCase(get()) }
    factory { DeletePhotosUseCase(get()) }
    factory { DeleteAlbumUseCase(get()) }
    factory { GetPhotoWithAlbumUseCase(get()) }
    factory { GetPreferencesUseCase(get()) }
    factory { SetCompareOverTutorialViewedUseCase(get()) }
    factory { GetLastPhotoByAlbumUseCase(get()) }
}