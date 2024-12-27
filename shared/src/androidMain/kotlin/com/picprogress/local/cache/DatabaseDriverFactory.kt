package com.picprogress.local.cache

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.picprogress.cache.PicProgressDatabase
import com.picprogress.local.data.cache.DatabaseDriverFactory

class DatabaseDriverFactoryImpl(private val context: Context): DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(PicProgressDatabase.Schema, context, "pp-database.db")
    }
}