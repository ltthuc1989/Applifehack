package com.ezyplanet.core.util.rxlifecycle




import android.content.Context
import android.location.Location
import org.reactivestreams.Publisher
import org.reactivestreams.Subscription

import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.CompletableTransformer
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Maybe
import io.reactivex.MaybeSource
import io.reactivex.MaybeTransformer
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import androidx.lifecycle.Lifecycle.Event.*


import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * Created by florentchampigny on 21/05/2017.
 */

 class RxLifecycle(private val lifecycle: Lifecycle) {

    private val subject = PublishSubject.create<Lifecycle.Event>().toSerialized()
    private val observer: RxLifecycleObserver

    init {
        this.observer = RxLifecycleObserver(subject)
        lifecycle.addObserver(observer)

    }

    fun onEvent(): Observable<Lifecycle.Event> {
        return subject
    }

    fun onCreate(): Observable<Lifecycle.Event> {
        return onEvent().filter { event -> ON_CREATE.equals(event) }
    }

    fun onStart(): Observable<Lifecycle.Event> {
        return onEvent().filter { event -> ON_START.equals(event) }
    }

    fun onResume(): Observable<Lifecycle.Event> {
        return onEvent().filter { event -> ON_RESUME.equals(event) }
    }

    fun onPause(): Observable<Lifecycle.Event> {
        return onEvent().filter { event -> ON_PAUSE.equals(event) }
    }

    fun onStop(): Observable<Lifecycle.Event> {
        return onEvent().filter { event -> ON_STOP.equals(event) }
    }

    fun onDestroy(): Observable<Lifecycle.Event> {
        return onEvent().filter { event -> ON_DESTROY.equals(event) }
    }

    fun onAny(): Observable<Lifecycle.Event> {
        return onEvent().filter { event -> ON_ANY.equals(event) }
    }

    fun <T> onlyIfResumedOrStarted(value: T): Observable<T> {
        return Observable.just<Any>(lifecycle)
                .flatMap {

                    val currentState =( it as Lifecycle).currentState
                    if (currentState.equals(Lifecycle.State.RESUMED) || currentState.equals(Lifecycle.State.STARTED)) {
                        Observable.just(value)
                    } else {
                        onResume()
                                .map { value }
                    }
                }
    }

    fun disposeOnDestroy(disposable: Disposable) {
        onDestroy()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { disposable.dispose() }
    }

    fun disposeOnStop(disposable: Disposable) {
        onStop()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { disposable.dispose() }
    }

    fun disposeOnPause(disposable: Disposable) {
        onPause()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { disposable.dispose() }
    }

    fun <T> disposeOnDestroy(): RxTransformer<T, T> {
        return object : RxTransformer<T, T>() {
            override fun apply(@NonNull upstream: Flowable<T>): Publisher<T> {
                return upstream.doOnSubscribe { subscription -> disposeOnDestroy(subscription) }
            }


            override fun apply(@NonNull upstream: Completable): CompletableSource {
                return upstream.doOnSubscribe { disposable -> disposeOnDestroy(disposable) }
            }

            override fun apply(@NonNull upstream: Single<T>): SingleSource<T> {
                return upstream.doOnSubscribe { disposable -> disposeOnDestroy(disposable) }
            }

            override fun apply(@NonNull upstream: Maybe<T>): MaybeSource<T> {
                return upstream.doOnSubscribe { disposable -> disposeOnDestroy(disposable) }
            }

            override fun apply(@NonNull upstream: Observable<T>): ObservableSource<T> {
                return upstream.doOnSubscribe { disposable -> disposeOnDestroy(disposable) }
            }
        }
    }

    fun <T> disposeOnPause(): RxTransformer<T, T> {
        return object : RxTransformer<T, T>() {
            override fun apply(@NonNull upstream: Flowable<T>): Publisher<T> {
                return upstream.doOnSubscribe { subscription -> disposeOnPause(subscription) }
            }


            override fun apply(@NonNull upstream: Completable): CompletableSource {
                return upstream.doOnSubscribe { disposable -> disposeOnPause(disposable) }
            }

            override fun apply(@NonNull upstream: Single<T>): SingleSource<T> {
                return upstream.doOnSubscribe { disposable -> disposeOnPause(disposable) }
            }

            override fun apply(@NonNull upstream: Maybe<T>): MaybeSource<T> {
                return upstream.doOnSubscribe { disposable -> disposeOnPause(disposable) }
            }

            override fun apply(@NonNull upstream: Observable<T>): ObservableSource<T> {
                return upstream.doOnSubscribe { disposable -> disposeOnPause(disposable) }
            }
        }
    }

    fun <T> disposeOnStop(): RxTransformer<T, T> {
        return object : RxTransformer<T, T>() {
            override fun apply(@NonNull upstream: Flowable<T>): Publisher<T> {
                return upstream.doOnSubscribe { subscription -> disposeOnStop(subscription) }
            }

            override fun apply(@NonNull upstream: Completable): CompletableSource {
                return upstream.doOnSubscribe { disposable -> disposeOnStop(disposable) }
            }

            override fun apply(@NonNull upstream: Single<T>): SingleSource<T> {
                return upstream.doOnSubscribe { disposable -> disposeOnStop(disposable) }
            }

            override fun apply(@NonNull upstream: Maybe<T>): MaybeSource<T> {
                return upstream.doOnSubscribe { disposable -> disposeOnStop(disposable) }
            }

            override fun apply(@NonNull upstream: Observable<T>): ObservableSource<T> {
                return upstream.doOnSubscribe { disposable -> disposeOnStop(disposable) }
            }
        }
    }


    fun <T> disposeOn(disposeEvent: DISPOSE_EVENT): RxTransformer<T, T> {
        return object : RxTransformer<T, T>() {
            override fun apply(@NonNull upstream: Flowable<T>): Publisher<T> {
                return upstream.doOnSubscribe { disposeOn<Any>(disposeEvent) }
            }

            override fun apply(@NonNull upstream: Completable): CompletableSource {
                return upstream.doOnSubscribe { disposeOn<Any>(disposeEvent) }
            }

            override fun apply(@NonNull upstream: Single<T>): SingleSource<T> {
                return upstream.doOnSubscribe { disposeOn<Any>(disposeEvent) }
            }

            override fun apply(@NonNull upstream: Maybe<T>): MaybeSource<T> {
                return upstream.doOnSubscribe { disposeOn<Any>(disposeEvent) }
            }

            override fun apply(@NonNull upstream: Observable<T>): ObservableSource<T> {
                return upstream.doOnSubscribe { disposeOn<Any>(disposeEvent) }
            }
        }
    }


    fun disposeOnDestroy(subscription: Subscription) {
        onDestroy()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { subscription.cancel() }
    }

    fun disposeOnStop(subscription: Subscription) {
        onStop()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { subscription.cancel() }
    }

    fun disposeOnPause(subscription: Subscription) {
        onPause()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { subscription.cancel() }
    }

    abstract inner class RxTransformer<U, D> : ObservableTransformer<U, D>, SingleTransformer<U, D>, MaybeTransformer<U, D>, CompletableTransformer, FlowableTransformer<U, D>

    enum class DISPOSE_EVENT {
        DESTROY,
        STOP,
        PAUSE
    }

    companion object {

        fun with(lifecycleOwner: LifecycleOwner): RxLifecycle {
            return RxLifecycle(lifecycleOwner.getLifecycle())
        }

        fun with(lifecycle: Lifecycle): RxLifecycle {
            return RxLifecycle(lifecycle)
        }

        fun with(lifecycleActivity: AppCompatActivity): RxLifecycle {
            return RxLifecycle(lifecycleActivity.getLifecycle())
        }

        fun with(lifecycleFragment: Fragment): RxLifecycle {
            return RxLifecycle(lifecycleFragment.getLifecycle())
        }

        fun onEvent(lifecycle: Lifecycle): Observable<Lifecycle.Event> {
            return RxLifecycle.with(lifecycle).onEvent()
        }

        fun onEvent(lifecycle: LifecycleOwner): Observable<Lifecycle.Event> {
            return RxLifecycle.with(lifecycle).onEvent()
        }

        fun onCreate(lifecycle: Lifecycle): Observable<Lifecycle.Event> {
            return RxLifecycle.with(lifecycle).onCreate()
        }

        fun onCreate(lifecycle: LifecycleOwner): Observable<Lifecycle.Event> {
            return RxLifecycle.with(lifecycle).onCreate()
        }

        fun onStart(lifecycle: Lifecycle): Observable<Lifecycle.Event> {
            return RxLifecycle.with(lifecycle).onStart()
        }

        fun onStart(lifecycle: LifecycleOwner): Observable<Lifecycle.Event> {
            return RxLifecycle.with(lifecycle).onStart()
        }

        fun onResume(lifecycle: Lifecycle): Observable<Lifecycle.Event> {
            return RxLifecycle.with(lifecycle).onResume()
        }

        fun onResume(lifecycle: LifecycleOwner): Observable<Lifecycle.Event> {
            return RxLifecycle.with(lifecycle).onResume()
        }

        fun onPause(lifecycle: Lifecycle): Observable<Lifecycle.Event> {
            return RxLifecycle.with(lifecycle).onPause()
        }

        fun onPause(lifecycle: LifecycleOwner): Observable<Lifecycle.Event> {
            return RxLifecycle.with(lifecycle).onPause()
        }

        fun onStop(lifecycle: Lifecycle): Observable<Lifecycle.Event> {
            return RxLifecycle.with(lifecycle).onStop()
        }

        fun onStop(lifecycle: LifecycleOwner): Observable<Lifecycle.Event> {
            return RxLifecycle.with(lifecycle).onStop()
        }

        fun onDestroy(lifecycle: Lifecycle): Observable<Lifecycle.Event> {
            return RxLifecycle.with(lifecycle).onDestroy()
        }

        fun onDestroy(lifecycle: LifecycleOwner): Observable<Lifecycle.Event> {
            return RxLifecycle.with(lifecycle).onDestroy()
        }

        fun onAny(lifecycle: Lifecycle): Observable<Lifecycle.Event> {
            return RxLifecycle.with(lifecycle).onAny()
        }

        fun onAny(lifecycle: LifecycleOwner): Observable<Lifecycle.Event> {
            return RxLifecycle.with(lifecycle).onAny()
        }

        fun <T> onlyIfResumedOrStarted(lifecycleOwner: LifecycleOwner, value: T): Observable<T> {
            return RxLifecycle.with(lifecycleOwner).onlyIfResumedOrStarted(value)
        }

        fun <T> onlyIfResumedOrStarted(lifecycle: Lifecycle, value: T): Observable<T> {
            return RxLifecycle.with(lifecycle).onlyIfResumedOrStarted(value)
        }

        fun disposeOnDestroy(lifecycleOwner: LifecycleOwner, disposable: Disposable) {
            RxLifecycle.with(lifecycleOwner).disposeOnDestroy(disposable)
        }

        fun disposeOnDestroy(lifecycle: Lifecycle, disposable: Disposable) {
            RxLifecycle.with(lifecycle).disposeOnDestroy(disposable)
        }

        fun disposeOn(lifecycle: Lifecycle, disposeEvent: DISPOSE_EVENT, disposable: Disposable) {
            when (disposeEvent) {
                RxLifecycle.DISPOSE_EVENT.STOP -> disposeOnStop(lifecycle, disposable)
                RxLifecycle.DISPOSE_EVENT.PAUSE -> disposeOnPause(lifecycle, disposable)
                RxLifecycle.DISPOSE_EVENT.DESTROY -> disposeOnDestroy(lifecycle, disposable)
            }
        }

        fun disposeOn(lifecycleOwner: LifecycleOwner, disposeEvent: DISPOSE_EVENT, disposable: Disposable) {
            disposeOn(lifecycleOwner.getLifecycle(), disposeEvent, disposable)
        }

        fun disposeOnDestroy(lifecycleOwner: LifecycleOwner, subscription: Subscription) {
            RxLifecycle.with(lifecycleOwner).disposeOnDestroy(subscription)
        }

        fun disposeOnDestroy(lifecycle: Lifecycle, subscription: Subscription) {
            RxLifecycle.with(lifecycle).disposeOnDestroy(subscription)
        }

        fun disposeOnStop(lifecycleOwner: LifecycleOwner, disposable: Disposable) {
            RxLifecycle.with(lifecycleOwner).disposeOnStop(disposable)
        }

        fun disposeOnStop(lifecycle: Lifecycle, disposable: Disposable) {
            RxLifecycle.with(lifecycle).disposeOnStop(disposable)
        }

        fun disposeOnStop(lifecycleOwner: LifecycleOwner, subscription: Subscription) {
            RxLifecycle.with(lifecycleOwner).disposeOnStop(subscription)
        }

        fun disposeOnStop(lifecycle: Lifecycle, subscription: Subscription) {
            RxLifecycle.with(lifecycle).disposeOnStop(subscription)
        }

        fun disposeOnPause(lifecycleOwner: LifecycleOwner, disposable: Disposable) {
            RxLifecycle.with(lifecycleOwner).disposeOnPause(disposable)
        }

        fun disposeOnPause(lifecycle: Lifecycle, disposable: Disposable) {
            RxLifecycle.with(lifecycle).disposeOnPause(disposable)
        }

        fun disposeOnPause(lifecycleOwner: LifecycleOwner, subscription: Subscription) {
            RxLifecycle.with(lifecycleOwner).disposeOnPause(subscription)
        }

        fun disposeOnPause(lifecycle: Lifecycle, subscription: Subscription) {
            RxLifecycle.with(lifecycle).disposeOnPause(subscription)
        }

        fun <T> disposeOn(lifecycle: Lifecycle, disposeEvent: DISPOSE_EVENT): RxTransformer<T, T> {
            return RxLifecycle.with(lifecycle).disposeOn(disposeEvent)
        }

        fun <T> disposeOn(lifecycleOwner: LifecycleOwner, disposeEvent: DISPOSE_EVENT): RxTransformer<T, T> {
            return disposeOn(lifecycleOwner.getLifecycle(), disposeEvent)
        }

        fun <T> disposeOnDestroy(lifecycle: Lifecycle): RxTransformer<T, T> {
            return RxLifecycle.with(lifecycle).disposeOnDestroy()
        }

        fun <T> disposeOnDestroy(lifecycleOwner: LifecycleOwner): RxTransformer<T, T> {
            return RxLifecycle.with(lifecycleOwner).disposeOnDestroy()
        }

        fun <T> disposeOnPause(lifecycle: Lifecycle): RxTransformer<T, T> {
            return RxLifecycle.with(lifecycle).disposeOnPause()
        }

        fun <T> disposeOnPause(lifecycleOwner: LifecycleOwner): RxTransformer<T, T> {
            return RxLifecycle.with(lifecycleOwner).disposeOnPause()
        }

        fun <T> disposeOnStop(lifecycle: Lifecycle): RxTransformer<T, T> {
            return RxLifecycle.with(lifecycle).disposeOnStop()
        }

        fun <T> disposeOnStop(lifecycleOwner: LifecycleOwner): RxTransformer<T, T> {
            return RxLifecycle.with(lifecycleOwner).disposeOnStop()
        }
    }
}

