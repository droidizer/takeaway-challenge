package com.guru.takeaway.schedulers

import com.guru.takeaway.ui.utils.ISchedulersProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class TestSchedulerProvider : ISchedulersProvider {
    override fun getComputationScheduler(): Scheduler = Schedulers.trampoline()

    override fun getIOScheduler(): Scheduler = Schedulers.trampoline()

    override fun getUIScheduler(): Scheduler = Schedulers.trampoline()

    override fun getNewThreadScheduler() = Schedulers.trampoline()
}