package com.guru.takeaway.di

import com.guru.takeaway.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface Contributors {

    @ContributesAndroidInjector
    fun injectMainActivity(): MainActivity
}
