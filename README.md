**Rxjava3！**

**Rxjava3！**

**Rxjava3！**

目前只适用于Rxjava3的工具类，帮助开发者快速串行执行多任务，并行执行多任务的，以及间隔计时器，倒计时器

#### 1.How to use

**Step 1.** Add the JitPack repository to your build file

```css
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

**Step 2.** Add the dependency

```css
dependencies {
	        implementation 'com.github.MiracleKael:RxUtils:v1.0'
	}
```

**Step 3.** If you want to use it anywhere else，you need add  the dependency

```css
	dependencies {
	        //支持rxjava3的Rxlife
    		implementation 'com.github.liujingxing.rxlife:rxlife-rxjava3:2.1.0'
	}
```

**Step 4.** You can use in AppCompatActivity or Fragment 

```
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
```

**Step 4.** You can use in other Class, just need extends BaseScope

```
public class MainPresenter extends BaseScope {
    private static final String TAG = "MainPresenter";
    public MainPresenter(LifecycleOwner owner) {
        super(owner);
        
    }
    public void test (){
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
    
}
```

#### 2.Method description

1.倒计时器

```
fun countDownTimer(duration: Long, timeUnit: TimeUnit, view: View, callBack: TimerCallBack) {
```

```
fun countDownTimer(duration: Long, timeUnit: TimeUnit, scope: Scope, callBack: TimerCallBack) {
```

```
fun countDownTimer(duration: Long, timeUnit: TimeUnit, lifecycleOwner: LifecycleOwner, callBack: TimerCallBack) {
```

duration:倒计时长

timeUnit:时长单位

scope:传入一个继承BaseScope的类即可

lifecycleOwner：LifecycleOwner



2.计时器

```
fun timer(intervalTime: Long, frequency: Long, timeUnit: TimeUnit, view: View, callBack: TimerCallBack) {
```

```
fun timer(intervalTime: Long, frequency: Long, timeUnit: TimeUnit, scope: Scope, callBack: TimerCallBack) {
```

```
fun timer(intervalTime: Long, frequency: Long, timeUnit: TimeUnit, lifecycleOwner: LifecycleOwner, callBack: TimerCallBack) {
```

intervalTime：间隔时长

frequency：执行次数

timeUnit：时长单位



3.多任务串行执行：

```
fun serialExecute(vararg args: Observable<*>?, view: View, callBack: ResultCallBack) {
```

```
fun serialExecute(vararg args: Observable<*>?, scope: Scope, callBack: ResultCallBack) {
```

```
fun serialExecute(vararg args: Observable<*>?, lifecycleOwner: LifecycleOwner, callBack: ResultCallBack) {
```

args：可变参数，传入数个任务Observable的数组即可

只有任务都完成了，才会返回onSuccess()



4.多任务并行执行

```
fun parallelExecute(vararg args: Observable<*>?, view: View, callBack: ResultCallBack) {
```

```
fun parallelExecute(vararg args: Observable<*>?, scope: Scope, callBack: ResultCallBack) {
```

```
fun parallelExecute(vararg args: Observable<*>?, lifecycleOwner: LifecycleOwner, callBack: ResultCallBack) {
```

args：可变参数，传入数个任务Observable的数组即可

只有任务都完成了，才会返回onSuccess()
