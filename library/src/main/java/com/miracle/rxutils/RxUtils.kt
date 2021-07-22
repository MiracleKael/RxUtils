package com.miracle.rxutils

import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.rxjava.rxlife.Scope
import com.rxjava.rxlife.life
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Rx工具类；
 * 传入LifecycleTransformer用于绑定生命周期，防止内存泄漏
 */
object RxUtils {
    private const val TAG = "RxUtils"

    /**
     * 默认是会随着界面生命周期暂停而销毁的倒计时器，
     * 可通过传入不同的LifecycleTransformer控制销毁的时机而达到回到桌面不停止计时的目的
     * @param duration:时长
     * @param timeUnit 时长单位
     */
    fun countDownTimer(duration: Long, timeUnit: TimeUnit, view: View, callBack: TimerCallBack) {
        Observable.intervalRange(1, duration, 1, 1, timeUnit)
                .doOnDispose {
                    Log.d(TAG, "countDownTimer dispose")
                }
                .doOnSubscribe {
                    callBack.onStart()
                }
                .doOnComplete {
                    callBack.onFinish()
                }
                .life(view)
                .subscribe {
                    callBack.onNext(it)
                }
    }

    fun countDownTimer(duration: Long, timeUnit: TimeUnit, scope: Scope, callBack: TimerCallBack) {
        Observable.intervalRange(1, duration, 1, 1, timeUnit)
                .doOnDispose {
                    Log.d(TAG, "countDownTimer dispose")
                }
                .doOnSubscribe {
                    callBack.onStart()
                }
                .doOnComplete {
                    callBack.onFinish()
                }
                .life(scope)
                .subscribe {
                    callBack.onNext(it)
                }
    }

    fun countDownTimer(duration: Long, timeUnit: TimeUnit, lifecycleOwner: LifecycleOwner, callBack: TimerCallBack) {
        Observable.intervalRange(1, duration, 1, 1, timeUnit)
                .doOnDispose {
                    Log.d(TAG, "countDownTimer dispose")
                }
                .doOnSubscribe {
                    callBack.onStart()
                }
                .doOnComplete {
                    callBack.onFinish()
                }
                .life(lifecycleOwner)
                .subscribe {
                    callBack.onNext(it)
                }
    }

    /**
     * 计时器
     * @param intervalTime 间隔时间
     * @param frequency 执行的次数
     * @param timeUnit 时间单位
     */
    fun timer(intervalTime: Long, frequency: Long, timeUnit: TimeUnit, view: View, callBack: TimerCallBack) {
        Observable.interval(intervalTime, intervalTime, timeUnit)
                .take(frequency)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose {
                    Log.d(TAG, "timer dispose")
                }
                .life(view)
                .subscribe(object : Observer<Long> {
                    override fun onSubscribe(d: Disposable?) {
                        callBack.onStart()
                    }

                    override fun onNext(t: Long?) {
                        if (t != null) {
                            callBack.onNext(t)
                        }
                    }

                    override fun onError(e: Throwable?) {
                        Log.e(TAG, "timer onError==" + e?.message)
                        callBack.onFinish()
                    }

                    override fun onComplete() {
                        callBack.onFinish()
                    }
                })
    }

    /**
     * 计时器
     * @param intervalTime 间隔时间
     * @param frequency 执行的次数
     * @param timeUnit 时间单位
     */
    fun timer(intervalTime: Long, frequency: Long, timeUnit: TimeUnit, scope: Scope, callBack: TimerCallBack) {
        Observable.interval(intervalTime, intervalTime, timeUnit)
                .take(frequency)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose {
                    Log.d(TAG, "timer dispose")
                }
                .life(scope)
                .subscribe(object : Observer<Long> {
                    override fun onSubscribe(d: Disposable?) {
                        callBack.onStart()
                    }

                    override fun onNext(t: Long?) {
                        if (t != null) {
                            callBack.onNext(t)
                        }
                    }

                    override fun onError(e: Throwable?) {
                        Log.e(TAG, "timer onError==" + e?.message)
                        callBack.onFinish()
                    }

                    override fun onComplete() {
                        callBack.onFinish()
                    }
                })
    }

    /**
     * 计时器
     * @param intervalTime 间隔时间
     * @param frequency 执行的次数
     * @param timeUnit 时间单位
     */
    fun timer(intervalTime: Long, frequency: Long, timeUnit: TimeUnit, lifecycleOwner: LifecycleOwner, callBack: TimerCallBack) {
        Observable.interval(intervalTime, intervalTime, timeUnit)
                .take(frequency)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose {
                    Log.d(TAG, "timer dispose")
                }
                .life(lifecycleOwner)
                .subscribe(object : Observer<Long> {
                    override fun onSubscribe(d: Disposable?) {
                        callBack.onStart()
                    }

                    override fun onNext(t: Long?) {
                        if (t != null) {
                            callBack.onNext(t)
                        }
                    }

                    override fun onError(e: Throwable?) {
                        Log.e(TAG, "timer onError==" + e?.message)
                        callBack.onFinish()
                    }

                    override fun onComplete() {
                        callBack.onFinish()
                    }
                })
    }

    /**
     * 串行执行多个任务，按传入顺序逐个执行,一个执行完了才开始执行下一个，都执行完了，才会回调成功
     * @param args:多个Observable类型任务
     */
    fun serialExecute(vararg args: Observable<*>?, view: View, callBack: ResultCallBack) {
        if (args.isNotEmpty()) {
            Observable.fromArray(*args)
                    .subscribeOn(Schedulers.io())
                    .concatMap { observable ->
                        observable!!.subscribeOn(Schedulers.io())
                    }.toList()
                    .toObservable()
                    .doOnDispose {
                        Log.d(TAG, "serialExecute() dispose")
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .life(view)
                    .subscribe({
                        callBack.onSuccess()
                    }, {
                        callBack.onError(it)
                    })
        }
    }

    /**
     * 串行执行多个任务，按传入顺序逐个执行,一个执行完了才开始执行下一个，都执行完了，才会回调成功
     * @param args:多个Observable类型任务
     */
    fun serialExecute(vararg args: Observable<*>?, scope: Scope, callBack: ResultCallBack) {
        if (args.isNotEmpty()) {
            Observable.fromArray(*args)
                    .subscribeOn(Schedulers.io())
                    .concatMap { observable ->
                        observable!!.subscribeOn(Schedulers.io())
                    }.toList()
                    .toObservable()
                    .doOnDispose {
                        Log.d(TAG, "serialExecute() dispose")
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .life(scope)
                    .subscribe({
                        callBack.onSuccess()
                    }, {
                        callBack.onError(it)
                    })
        }
    }

    /**
     * 串行执行多个任务，按传入顺序逐个执行,一个执行完了才开始执行下一个，都执行完了，才会回调成功
     * @param args:多个Observable类型任务
     */
    fun serialExecute(vararg args: Observable<*>?, lifecycleOwner: LifecycleOwner, callBack: ResultCallBack) {
        if (args.isNotEmpty()) {
            Observable.fromArray(*args)
                    .subscribeOn(Schedulers.io())
                    .concatMap { observable ->
                        observable!!.subscribeOn(Schedulers.io())
                    }.toList()
                    .toObservable()
                    .doOnDispose {
                        Log.d(TAG, "serialExecute() dispose")
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .life(lifecycleOwner)
                    .subscribe({
                        callBack.onSuccess()
                    }, {
                        callBack.onError(it)
                    })
        }
    }

    /**
     * 并行执行多个，都执行完才会回调成功
     * @param args:多个Observable类型任务
     */
    fun parallelExecute(vararg args: Observable<*>?, view: View, callBack: ResultCallBack) {
        if (args.isNotEmpty()) {
            Observable.fromArray(*args)
                    .flatMap { observable ->
                        observable!!.subscribeOn(Schedulers.io())
                    }.toList()
                    .toObservable()
                    .doOnDispose {
                        Log.d(TAG, "parallelExecute() dispose")
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .life(view)
                    .subscribe({
                        callBack.onSuccess()
                    }, {
                        callBack.onError(it)
                    })
        }
    }

    /**
     * 并行执行多个，都执行完才会回调成功
     * @param args:多个Observable类型任务
     */
    fun parallelExecute(vararg args: Observable<*>?, scope: Scope, callBack: ResultCallBack) {
        if (args.isNotEmpty()) {
            Observable.fromArray(*args)
                    .flatMap { observable ->
                        observable!!.subscribeOn(Schedulers.io())
                    }.toList()
                    .toObservable()
                    .doOnDispose {
                        Log.d(TAG, "parallelExecute() dispose")
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .life(scope)
                    .subscribe({
                        callBack.onSuccess()
                    }, {
                        callBack.onError(it)
                    })
        }
    }

    /**
     * 并行执行多个，都执行完才会回调成功
     * @param args:多个Observable类型任务
     */
    fun parallelExecute(vararg args: Observable<*>?, lifecycleOwner: LifecycleOwner, callBack: ResultCallBack) {
        if (args.isNotEmpty()) {
            Observable.fromArray(*args)
                    .flatMap { observable ->
                        observable!!.subscribeOn(Schedulers.io())
                    }.toList()
                    .toObservable()
                    .doOnDispose {
                        Log.d(TAG, "parallelExecute() dispose")
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .life(lifecycleOwner)
                    .subscribe({
                        callBack.onSuccess()
                    }, {
                        callBack.onError(it)
                    })
        }
    }


    interface ResultCallBack {
        fun onSuccess()
        fun onError(e: Throwable)
    }

    interface TimerCallBack {
        fun onStart()
        fun onNext(time: Long)
        fun onFinish()
    }
}
