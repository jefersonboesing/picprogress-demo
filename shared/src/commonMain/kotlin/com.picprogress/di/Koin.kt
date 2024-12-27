package com.picprogress.di

import com.picprogress.local.di.localModule
import com.picprogress.repository.di.repositoryModule
import com.picprogress.usecase.di.useCaseModule
import com.picprogress.util.di.utilModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun initSharedKoin(platformConfiguration: (KoinApplication.() -> (Unit))? = null) = startKoin {
    platformConfiguration?.invoke(this)
    modules(platformModules)
    modules(repositoryModule, useCaseModule, localModule, utilModule)
}