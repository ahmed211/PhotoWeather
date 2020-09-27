package com.robusta.photoweatherapp.di

import com.robusta.photoweatherapp.di.module.AppModule
import com.robusta.photoweatherapp.di.module.NetModule
import com.robusta.photoweatherapp.presentation.view.WeatherActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetModule::class])
interface NetComponent {
    fun inject(weatherActivity: WeatherActivity)
}