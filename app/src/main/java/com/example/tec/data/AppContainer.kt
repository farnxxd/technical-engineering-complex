package com.example.tec.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val tecRepository: TecRepository
}

/**
 * [AppContainer] implementation that provides instance of [TecRepository].
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [TecRepository].
     */
    override val tecRepository: TecRepository by lazy {
        TecRepository(
            memberDao = TecDatabase.getDatabase(context).memberDao(),
            interestDao = TecDatabase.getDatabase(context).interestDao()
        )
    }
}