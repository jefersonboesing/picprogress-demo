package com.picprogress.local.di

import com.picprogress.local.data.cache.DatabaseDriverFactory
import com.picprogress.local.cache.DatabaseDriverFactoryImpl
import com.picprogress.local.storage.AndroidFileStorageImpl
import com.picprogress.local.storage.FileStorage
import org.koin.dsl.module

val platformLocalModule = module {
    single<DatabaseDriverFactory> { DatabaseDriverFactoryImpl(get()) }
    single<FileStorage> { AndroidFileStorageImpl(get()) }
}