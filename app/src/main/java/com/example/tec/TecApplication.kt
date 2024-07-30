package com.example.tec

import android.app.Application
import com.example.tec.data.AppContainer
import com.example.tec.data.AppDataContainer

class TecApplication : Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies.
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}