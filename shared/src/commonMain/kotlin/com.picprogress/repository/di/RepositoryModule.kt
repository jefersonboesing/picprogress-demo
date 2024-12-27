package com.picprogress.repository.di

import com.picprogress.repository.AlbumsRepository
import com.picprogress.repository.AlbumsRepositoryImpl
import com.picprogress.repository.PreferencesRepository
import com.picprogress.repository.PreferencesRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<AlbumsRepository> { AlbumsRepositoryImpl(get(), get(), get(), get()) }
    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }
}