package ru.gaket.themoviedb.ru.gaket.themoviedb.core

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface SchedulerProvider {

    fun ui(): Scheduler

    fun background(): Scheduler

    fun time(): Scheduler

    fun single(): Scheduler

    class Impl : SchedulerProvider {

        override fun ui(): Scheduler = AndroidSchedulers.mainThread()

        override fun background(): Scheduler = Schedulers.io()

        override fun time(): Scheduler = Schedulers.computation()

        override fun single(): Scheduler = Schedulers.single()
    }
}
