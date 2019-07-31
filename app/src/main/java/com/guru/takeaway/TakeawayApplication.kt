package com.guru.takeaway

import com.guru.takeaway.di.DaggerTakeawayComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class TakeawayApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
       return DaggerTakeawayComponent.builder().application(this).build()
    }
}