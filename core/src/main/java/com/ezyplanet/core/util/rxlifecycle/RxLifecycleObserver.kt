package com.ezyplanet.core.util.rxlifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.subjects.Subject
import androidx.lifecycle.Lifecycle.Event.*

internal class RxLifecycleObserver(private val subject: Subject<Lifecycle.Event>) : LifecycleObserver {

    @OnLifecycleEvent(ON_CREATE)
    fun onViewCreated() {
        subject.onNext(ON_CREATE)
    }

    @OnLifecycleEvent(ON_START)
    fun onViewStarted() {
        subject.onNext(ON_START)
    }

    @OnLifecycleEvent(ON_RESUME)
    fun onViewResumed() {
        subject.onNext(ON_RESUME)
    }

    @OnLifecycleEvent(ON_PAUSE)
    fun onViewPaused() {
        subject.onNext(ON_PAUSE)
    }

    @OnLifecycleEvent(ON_STOP)
    fun onViewStopped() {
        subject.onNext(ON_STOP)
    }

    @OnLifecycleEvent(ON_DESTROY)
    fun onViewDestroyed() {
        subject.onNext(ON_DESTROY)
    }

    @OnLifecycleEvent(ON_ANY)
    fun onViewEvent() {
        subject.onNext(ON_ANY)
    }
}