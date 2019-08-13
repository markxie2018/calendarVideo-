package markxie.game.calendarvideo

import android.app.Application
import markxie.game.calendarvideo.di.appModule
import markxie.game.calendarvideo.di.appViewModelModule
import markxie.game.calendarvideo.di.localModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import kotlin.properties.Delegates

class App : Application() {

    companion object {
        var instance by Delegates.notNull<App>()
    }


    override fun onCreate() {
        super.onCreate()
        instance = this


        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(appModule, appViewModelModule, localModule))
        }


    }
}