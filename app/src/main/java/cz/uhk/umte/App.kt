package cz.uhk.umte

import android.app.Application
import cz.uhk.umte.di.appModules
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // First code called here when app starts
        startKoin {
            androidContext(applicationContext)
            androidLogger(
                if (BuildConfig.DEBUG) Level.DEBUG
                else Level.NONE
            )
            modules(appModules)
        }
    }
}