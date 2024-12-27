package com.picprogress.util.di

import com.picprogress.util.TimeFrameProvider
import com.picprogress.util.TimeFrameProviderImpl
import org.koin.dsl.module

val utilModule = module {
    single<TimeFrameProvider> { TimeFrameProviderImpl() }
}