package com.rxdemo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.rxdemo.bean.Employee;
import com.rxdemo.bean.Mission;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    /**
     * Hello World!
     */
    private ImageView mIdIgview;
    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        // loadNetData();
        rxFlatMap();
    }

    private void showImagew() {
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getResources().getDrawable(R.drawable.splash);
                /*把Drawable对象发送出去*/
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        })
                .subscribe(new Subscriber<Drawable>() {
                    @Override
                    public void onCompleted() {
                        Logger.i(TAG, "onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i(TAG, "onError()");
                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        Logger.i(TAG, "onNext()");
                        mIdIgview.setImageDrawable(drawable);
                    }
                });
    }


    /*
     * 创建人：Yangshao
     * 创建时间：2016/12/28 13:57
     * @version
     * 在 io 线程加载一张网络图片
     * 加载完毕之后在主线程中显示到ImageView上。
     */
    private void loadNetData() {
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                try {
                    Drawable drawable = Drawable.createFromStream(new URL("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2502144641,437990411&fm=80&w=179&h=119&img.JPEG").openStream(), "src");
                    subscriber.onNext(drawable);
                } catch (IOException e) {
                    subscriber.onError(e);
                }

            }
        })  // 指定 subscribe() 所在的线程，也就是上面call()方法调用的线程
                .subscribeOn(Schedulers.io())
                // 指定 Subscriber 回调方法所在的线程，也就是onCompleted, onError, onNext回调的线程
                .observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Subscriber<Drawable>() {
                    @Override
                    public void onCompleted() {
                        Logger.i(TAG, "onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i(TAG, "onError()");
                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        Logger.i(TAG, "onNext()");
                        mIdIgview.setImageDrawable(drawable);
                    }
                });
    }

    /*
     * 创建人：Yangshao
     * 创建时间：2016/12/28 17:14
     * @version    变换
     */
    public void rxMap() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("https://ss2.baidu.com/-vo3dSag_xI4khGko9WTAnF6hhy/image/h%3D200/sign=4db5130a073b5bb5a1d727fe06d2d523/cf1b9d16fdfaaf51965f931e885494eef11f7ad6.jpg");
            }
        }).map(new Func1<String, Drawable>() {
            @Override
            public Drawable call(String url) {
                try {
                    Drawable drawable = Drawable.createFromStream(new URL(url).openStream(), "src");
                    return drawable;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Drawable>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i(TAG, "onError()");
                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        Logger.i(TAG, "onNext()");
                        mIdIgview.setImageDrawable(drawable);
                    }
                });
    }


    public void rxFlatMap() {
        List<Mission> msList1 = new ArrayList<>();
        List<Mission> msList2 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            msList1.add(new Mission("msList1 任务" + i));
            msList2.add(new Mission("msList2 任务" + i));
        }
        final List<Employee> list = new ArrayList<Employee>() {
        };
        list.add(new Employee("jackson", msList1));
        list.add(new Employee("sunny", msList2));
        Observable.from(list)
                .flatMap(new Func1<Employee, Observable<Mission>>() {
                    @Override
                    public Observable<Mission> call(Employee employee) {
                        return Observable.from(employee.getMissionList());
                    }
                })
                .subscribe(new Subscriber<Mission>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(Mission mission) {
                        Logger.i(mission.desc);
                    }
                });

    }


    private void initView() {
        mIdIgview = (ImageView) findViewById(R.id.id_igview);
    }
}
