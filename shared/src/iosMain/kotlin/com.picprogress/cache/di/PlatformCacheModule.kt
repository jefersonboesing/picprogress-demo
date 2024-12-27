package com.picprogress.cache.di

import com.picprogress.local.data.cache.DatabaseDriverFactory
import com.picprogress.cache.DatabaseDriverFactoryImpl
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.dsl.module

val platformCacheModule = module {
    single<DatabaseDriverFactory> { DatabaseDriverFactoryImpl() }
}