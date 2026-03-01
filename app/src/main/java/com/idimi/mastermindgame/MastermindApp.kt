package com.idimi.mastermindgame

import android.app.Application
import com.idimi.mastermindgame.di.appModule
import com.idimi.mastermindgame.di.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class MastermindApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MastermindApp)
            modules(listOf(appModule, databaseModule))
        }
        Timber.plant(Timber.DebugTree())
    }
}