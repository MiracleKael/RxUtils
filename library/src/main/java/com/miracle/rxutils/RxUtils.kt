package com.miracle.rxutils

import android.util.Log
import com.trello.rxlifecycle4.LifecycleTransformer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Action
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
     * @param duration:时长 单位s
     */
    fun countDownTimer(duration: Long, bindToLifecycle: LifecycleTransformer<Long>, callBack: TimerCallBack) {
        countDownTimer(duration, TimeUnit.SECONDS, bindToLifecycle, callBack)
    }

    /**
     * 默认是会随着界面生命周期暂停而销毁的倒计时器，
     * 可通过传入不同的LifecycleTransformer控制销毁的时机而达到回到桌面不停止计时的目的
     * @param duration:时长
     * @param timeUnit 时长单位
     */
    fun countDownTimer(duration: Long, timeUnit: TimeUnit, bindToLifecycle: LifecycleTransformer<Long>, callBack: TimerCallBack) {
        Observable.intervalRange(1, duration, 1, 1, timeUnit)
                .compose(bindToLifecycle)
                .doOnDispose {
                    Log.d(TAG, "timer dispose")
                }
                .doOnSubscribe {
                    callBack.onStart()
                }
                .doOnComplete {
                    callBack.onFinish()
                }
                .subscribe {
                    callBack.onNext(it)
                }
    }

    /**
     * 计时器
     * @param intervalTime 间隔时间
     * @param frequency 执行的次数
     */
    fun timer(intervalTime: Long, frequency: Long, bindToLifecycle: LifecycleTransformer<Long>, callBack: TimerCallBack) {
        timer(intervalTime, frequency, TimeUnit.SECONDS, bindToLifecycle, callBack)
    }

    /**
     * 计时器
     * @param intervalTime 间隔时间
     * @param frequency 执行的次数
     * @param timeUnit 时间单位
     */
    fun timer(intervalTime: Long, frequency: Long, timeUnit: TimeUnit, bindToLifecycle: LifecycleTransformer<Long>, callBack: TimerCallBack) {
        Observable.interval(intervalTime, intervalTime, timeUnit)
                .take(frequency)
                .compose(bindToLifecycle)
                .observeOn(AndroidSchedulers.mainThread())
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
    fun serialExecute(vararg args: Observable<*>?, bindToLifecycle: LifecycleTransformer<Any>, callBack: ResultCallBack) {
        if (args.isNotEmpty()) {
            Observable.fromArray(*args)
                    .subscribeOn(Schedulers.io())
                    .concatMap { observable ->
                        observable!!.subscribeOn(Schedulers.io())
                    }.toList()
                    .toObservable()
                    .doOnDispose(Action {
                        Log.d(TAG, "serialExecute() dispose")
                        callBack.dispose()
                    })
                    .compose(bindToLifecycle)
                    .observeOn(AndroidSchedulers.mainThread())
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
    fun parallelExecute(vararg args: Observable<*>?, bindToLifecycle: LifecycleTransformer<Any>, callBack: ResultCallBack) {
        if (args.isNotEmpty()) {
            Observable.fromArray(*args)
                    .flatMap { observable ->
                        observable!!.subscribeOn(Schedulers.io())
                    }.toList()
                    .toObservable()
                    .doOnDispose(Action {
                        Log.d(TAG, "serialExecute() dispose")
                    })
                    .compose(bindToLifecycle)
                    .observeOn(AndroidSchedulers.mainThread())
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
        fun dispose()
    }

    interface TimerCallBack {
        fun onStart()
        fun onNext(time: Long)
        fun onFinish()
    }
}
