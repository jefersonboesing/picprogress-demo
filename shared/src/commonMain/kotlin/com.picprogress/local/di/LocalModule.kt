package com.picprogress.local.di

import app.cash.sqldelight.EnumColumnAdapter
import com.picprogress.cache.PicProgressDatabase
import com.picprogress.local.data.cache.DatabaseDriverFactory
import com.picprogress.local.data.dataset.AlbumDataSet
import com.picprogress.local.data.dataset.AlbumDataSetImpl
import com.picprogress.local.data.dataset.PhotoDataSet
import com.picprogress.local.data.dataset.PhotoDataSetImpl
import com.picprogress.local.data.dataset.PreferencesDataSet
import com.picprogress.local.data.dataset.PreferencesDataSetImpl
import com.picprogress.local.data.mapper.AlbumTableToAlbumMapper
import com.picprogress.local.data.mapper.PhotoTableToPhotoMapper
import com.picprogress.local.data.mapper.PreferencesTableToPreferencesMapper
import compicprogress.AlbumTable
import org.koin.dsl.module

val localModule = module {
    single {
        PicProgressDatabase(
            driver = get<DatabaseDriverFactory>().createDriver(),
            albumTableAdapter = AlbumTable.Adapter(
                frequencyAdapter = EnumColumnAdapter()
            )
        )
    }

    // Queries
    single { get<PicProgressDatabase>().albumQueries }
    single { get<PicProgressDatabase>().photoQueries }
    single { get<PicProgressDatabase>().preferencesQueries }

    // Mapper
    factory { AlbumTableToAlbumMapper(get()) }
    factory { PhotoTableToPhotoMapper() }
    factory { PreferencesTableToPreferencesMapper() }

    // DataSet
    single<AlbumDataSet> { AlbumDataSetImpl(get(), get()) }
    single<PhotoDataSet> { PhotoDataSetImpl(get(), get()) }
    single<PreferencesDataSet> { PreferencesDataSetImpl(get(), get()) }

}