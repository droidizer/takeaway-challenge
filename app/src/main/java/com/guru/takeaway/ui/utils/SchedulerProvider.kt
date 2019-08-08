package com.guru.takeaway.ui.utils

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SchedulersProvider @Inject constructor() : ISchedulersProvider {
    override fun getUIScheduler() = AndroidSchedulers.mainThread()

    override fun getComputationScheduler() = Schedulers.computation()

    override fun getIOScheduler(): Scheduler = Schedulers.io()

    override fun getNewThreadScheduler(): Scheduler = Schedulers.newThread()
}