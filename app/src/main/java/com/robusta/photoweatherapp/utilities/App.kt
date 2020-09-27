package com.robusta.photoweatherapp.utilities

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.robusta.photoweatherapp.di.DaggerNetComponent
import com.robusta.photoweatherapp.di.module.AppModule
import com.robusta.photoweatherapp.di.NetComponent
import com.robusta.photoweatherapp.di.module.NetModule

class App : MultiDexApplication() {
    var netComponent: NetComponent? = null

    companion object {
        var context: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        context=this
        netComponent = DaggerNetComponent.builder()
            .appModule(AppModule(this))
            .netModule(NetModule())
            .build()


    }
}