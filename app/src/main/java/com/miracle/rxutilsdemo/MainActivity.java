package com.miracle.rxutilsdemo;


import android.os.Bundle;
import android.util.Log;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

import com.miracle.rxutils.RxUtils;

import org.jetbrains.annotations.NotNull;


import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Action;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Observable<String> observable1;
    private Observable<Integer> observable2;
    private Observable<String> observable3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initObservable();
    }


    public void timer(View view) {
        RxUtils.INSTANCE.timer(1, 10, TimeUnit.SECONDS,this, new RxUtils.TimerCallBack() {
            @Override
            public void onStart() {
                Log.i(TAG, "time== " + "onStart");
            }

            @Override
            public void onNext(long time) {
                Log.i(TAG, "time== " + time);
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "time== " + "onFinish");
            }
        });
    }

    public void countDownTimer(View view) {
        RxUtils.INSTANCE.countDownTimer(10, TimeUnit.SECONDS,this, new RxUtils.TimerCallBack() {
            @Override
            public void onStart() {
                Log.i(TAG, "countDownTimer== " + "onStart");
            }

            @Override
            public void onNext(long time) {
                Log.i(TAG, "countDownTimer== " + time);
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "countDownTimer== " + "onFinish");
            }
        });
    }

    public void parallelExecute(View view) {
        RxUtils.INSTANCE.parallelExecute(new Observable[]{observable1, observable2, observable3}, this, new RxUtils.ResultCallBack() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "?????????????????? ????????????");
            }

            @Override
            public void onError(@NotNull Throwable e) {
                Log.i(TAG, "?????????????????? ????????????");
            }
        });
    }

    public void oneByOne(View view) {
        RxUtils.INSTANCE.serialExecute(new Observable[]{observable1, observable2, observable3}, this, new RxUtils.ResultCallBack() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "?????????????????? ????????????");
            }

            @Override
            public void onError(@NotNull Throwable e) {
                Log.i(TAG, "?????????????????? ????????????");
            }
        });
    }

    private void initObservable() {
        observable1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NotNull ObservableEmitter<String> emitter) throws Exception {
                try {
                    Thread.sleep(3000);
                    emitter.onNext("");
                    emitter.onComplete();
                    Log.i(TAG, "observable1 ????????????");
                } catch (InterruptedException e) {

                } finally {
                    emitter.onNext("");
                    emitter.onComplete();
                }
            }
        }).doOnDispose(new Action() {
            @Override
            public void run() throws Throwable {
                Log.i(TAG, "observable1 doOnDispose");
            }
        });
        observable2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NotNull ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onComplete();
                Log.i(TAG, "observable2 ????????????");
            }
        }).doOnDispose(new Action() {
            @Override
            public void run() throws Throwable {
                Log.i(TAG, "observable2 doOnDispose");
            }
        });
        observable3 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NotNull ObservableEmitter<String> emitter) throws Exception {
                try {
                    Thread.sleep(2000);
                    emitter.onNext("");
                    emitter.onComplete();
                    Log.i(TAG, "observable3 ????????????");
                } catch (InterruptedException e) {

                } finally {
                    emitter.onNext("");
                    emitter.onComplete();
                }
            }
        }).doOnDispose(new Action() {
            @Override
            public void run() throws Throwable {
                Log.i(TAG, "observable3 doOnDispose");
            }
        });
    }

}