package com.guru.takeaway.ui.utils

import io.reactivex.Scheduler

interface ISchedulersProvider {
    fun getComputationScheduler(): Scheduler
    fun getIOScheduler(): Scheduler
    fun getUIScheduler(): Scheduler
    fun getNewThreadScheduler(): Scheduler
}