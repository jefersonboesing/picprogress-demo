package com.picprogress.cache

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.picprogress.local.data.cache.DatabaseDriverFactory

class DatabaseDriverFactoryImpl: DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(PicProgressDatabase.Schema, "pp-database.db")
    }
}