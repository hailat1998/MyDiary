package com.hd1998.mydiary

import android.app.Application
import com.hd1998.mydiary.di.appModule
import com.hd1998.mydiary.di.appmodule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class DairyApp : Application(){

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@DairyApp)
            modules(appModule)
        }
    }
}