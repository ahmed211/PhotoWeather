package com.robusta.photoweatherapp.di.module

import com.robusta.photoweatherapp.presentation.repository.datalayer.repository.ClientApiServices
import com.robusta.photoweatherapp.utilities.RemoteConfiguration.BASE_URL
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetModule {
    private var retrofit: Retrofit? = null
    private var apiServices: ClientApiServices? = null
    @Singleton
    @Provides
    fun provideApiService(): ClientApiServices {
        if (apiServices == null) apiServices = provideRetrofit().create(
            ClientApiServices::class.java
        )
        return apiServices as ClientApiServices
    }

    @Singleton
    @Provides
    fun  provideRetrofit(): Retrofit { // RemoteConfiguration.updateBaseURL(ChangeAPIServer.NONE);
        if (retrofit == null) {
            var client = OkHttpClient()
            client = OkHttpClient.Builder().addInterceptor { chain ->
                val request: Request =
                    chain.request().newBuilder().build()
                chain.proceed(request)
            }.build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
        return retrofit as Retrofit
    }
}