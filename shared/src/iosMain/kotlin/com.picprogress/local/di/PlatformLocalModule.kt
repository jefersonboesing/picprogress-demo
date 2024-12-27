package com.picprogress.local.di

import com.picprogress.local.storage.FileStorage
import com.picprogress.local.storage.iOSFileStorage
import org.koin.dsl.module

val platformLocalModule = module {
    single<FileStorage> { iOSFileStorage() }
}