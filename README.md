**Rxjava3！**

**Rxjava3！**

**Rxjava3！**

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





