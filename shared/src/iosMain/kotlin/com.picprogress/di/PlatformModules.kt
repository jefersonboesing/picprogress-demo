package com.picprogress.di

import com.picprogress.cache.di.platformCacheModule
import com.picprogress.local.di.platformLocalModule
import com.picprogress.viewmodel.di.platformViewModelModule
import org.koin.core.module.Module

actual val platformModules: List<Module> = listOf(
    platformCacheModule,
    platformViewModelModule,
    platformLocalModule
)
