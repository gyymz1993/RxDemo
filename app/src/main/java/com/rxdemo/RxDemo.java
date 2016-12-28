package com.rxdemo;

import com.orhanobut.logger.Logger;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/12/28.
 */
public class RxDemo {

    /*
     * 创建时间：2016/12/28 16:41
     * @version  事件订阅
     */
    public void onSubscribe() {
        /*事件订阅*/
        mObservable.subscribe(mObserver);
        mObservable.subscribe(mSubscriber);
        /*根据传入动作系统自动创建Subscriber*/
        mObservable.subscribe(mOnNextAction1, mOnErrorAction1, mOnCompletedAction0);
    }

    /*
 * 创建人：Yangshao
 * 创建时间：2016/12/28 9:41
 * @version
 *  这段代码可以理解为， Observable 发出了一个类型为 String ，
 *  值为 “Hello World!” 的事件，仅此而已。
 */
    Observable<String> mObservable;
    private void createObservable() {
        // 创建一个Observable
        mObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                /*发送一个事件*/
                subscriber.onNext("Observable Hellow");
                /*发送完成标记*/
                subscriber.onCompleted();
                Logger.e("Observable Hellow");
            }
        });

        /*这段代码等同于上面的代码*/
        mObservable = Observable.just("Observable Hellow");
    }

    /*
     * 创建人：Yangshao
     * 创建时间：2016/12/28 9:43
     * @version
     * 事件消费
     * 有事件产生，自然也要有事件消费。RxJava 可以通过 subscribe 操作符，
     * 对上述事件进行消费。首先，先创建一个观察者。
     *
     */
    Observer mObserver;
    Subscriber mSubscriber;
    public void createObserver() {
        /*创建一个观察者*/
        mObserver = new Observer() {
            /*事件全部处理完成后回调 */
            @Override
            public void onCompleted() {
                Logger.i("Observer onCompleted");
            }

            /*事件处理异常回调 */
            @Override
            public void onError(Throwable e) {
                Logger.i("Observer onError");
            }

            /*每接收到一个事件，回调一次*/
            @Override
            public void onNext(Object o) {
                Logger.i("Observer onNext"+o);
            }
        };

        /*创建观察者，等同于上面*/
        mSubscriber = new Subscriber() {
            @Override
            public void onCompleted() {
                Logger.i("Subscriber onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Logger.i("Subscriber onError");
            }

            @Override
            public void onNext(Object o) {
                Logger.i("Subscriber onNext"+o);
            }
        };
    }

    /*
     * 创建人：Yangshao
     * 创建时间：2016/12/28 11:06
     * @version
     *    我们可以为 Subscriber 中的三种状态根据自身需要分别创建一个回调动作 Action
     */
    Action0 mOnCompletedAction0;
    Action1<String> mOnNextAction1;
    Action1<Throwable> mOnErrorAction1;
    public void subAction() {
        mOnCompletedAction0 = new Action0() {
            @Override
            public void call() {

            }
        };

        mOnNextAction1 = new Action1<String>() {
            @Override
            public void call(String s) {

            }
        };

        mOnErrorAction1 = new Action1<Throwable>() {
            @Override
            public void call(Throwable e) {

            }
        };
    }

    public void rxFrom(){
        Observable.from(new Integer[]{1,2,3,4,5})
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Logger.i("number :  "+integer);
                    }
                });
    }



    public void rxJust(){
        Observable.just(1,2,3,4,5)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Logger.e("number : "+integer);
                    }
                });
    }

    public void rxFilter(){
        Observable.from(new Integer[]{1,2,3,4,5,6,7,8})
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        // 偶数返回true，则表示剔除奇数
                        return integer % 2 == 0;
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Logger.e("number "+integer);
            }
        });
    }


    public void rxDoOnNext(){
        Observable.from(new Integer[]{1,2,3,4,5,6,7,8,9})
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer%2==0;
                    }
                })
                .take(3) // 最多保留三个，也就是最后剩三个偶数
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        // 在输出偶数之前输出它的hashCode
                        Logger.i("hahcode = " + integer.hashCode());
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Logger.e("number "+integer);
            }
        });
    }

}
