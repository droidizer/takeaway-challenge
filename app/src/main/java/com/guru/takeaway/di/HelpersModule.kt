package com.guru.takeaway.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guru.takeaway.TakeawayApplication
import com.guru.takeaway.ui.utils.ISchedulersProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HelpersModule {

    @Provides
    fun provideContext(app: TakeawayApplication): Context = app.applicationContext

    @Provides
    fun provideResources(context: Context) = context.resources

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun providesScheduler(): ISchedulersProvider {
        return ISchedulersProvider.DEFAULT
    }
}