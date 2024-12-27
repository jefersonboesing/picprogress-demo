package com.picprogress

import android.app.Application
import com.picprogress.di.initSharedKoin
import org.koin.android.ext.koin.androidContext

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initSharedKoin {
            androidContext(this@AppApplication)
        }
    }

}