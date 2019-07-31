package com.guru.takeaway.di

import com.guru.takeaway.TakeawayApplication
import com.guru.takeaway.ui.di.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = arrayOf(
        AndroidSupportInjectionModule::class,
        Contributors::class,
        ManagersModule::class,
        MainActivityModule::class,
        HelpersModule ::class
    )
)
interface TakeawayComponent : AndroidInjector<TakeawayApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: TakeawayApplication): Builder

        fun build(): TakeawayComponent
    }
}