package com.stuto.core.study.thread;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 原子操作类
 * 参考网址:<a href="https://blog.csdn.net/carson0408/article/details/79450434">点击</a>
 * @author 作者 : zyq
 * 创建时间：2019/2/25 10:30
 * @version 0.0.1
 */
public class Thread07Atomic {
    private static final AtomicReference<Count> ar=new AtomicReference();


    public static void main(String[] args) throws InterruptedException {

        /**
         * 测试非原子型的操作
         */
      /*  for (int i = 0; i < 100; i++) {
            Thread unSafe = new UnSafeThread();
            unSafe.start();
        }
        for(int i=0;i<25;i++) {

            System.out.println("累加100次，得到结果是：" + UnSafeThread.count);
            Thread.sleep(10);
        }*/
        /**
         * 用synchronized方法保证安全
         */
        /*for (int i = 0; i < 100; i++) {
            Thread synchronizedThread = new SynchronizedThread();
            synchronizedThread.start();
        }
        for(int i=0;i<25;i++) {

            System.out.println("累加100次，得到结果是：" + SynchronizedThread.count);
            Thread.sleep(10);
        }*/

        /**
         * 使用原子操作类
         */
        /*for (int i = 0; i < 100; i++) {
            Thread atomicIntegerThread = new AtomicIntegerThread();
            atomicIntegerThread.start();
        }
        for(int i=0;i<25;i++) {

            System.out.println("累加100次，得到结果是：" + AtomicIntegerThread.at);
            Thread.sleep(10);
        }*/

        /**
         * 测试atomicReference
         */
        Count count=new Count(1001);
        long start=System.currentTimeMillis();
        ar.set(count);
        System.out.println("你好，"+ar.get());
        Count count1=new Count(1002);
        ar.compareAndSet(count, count1);//内存值与count是一样的，所以值更新为count1
        System.out.println("我发生了改变："+ar.get());
        Count count2=new Count(1003);
        ar.compareAndSet(count, count2);//此时内存智为count1，与count不一致，所以无法更新，因此内存值依然为count1
        System.out.println("我发生了改变："+ar.get());



    }
}

/**
 * 不安全的线程,没有使用原子操作类的情况
 */
class UnSafeThread extends Thread{
    static  int count = 0;

    @Override
    public void run() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        count++;

    }
}


class SynchronizedThread extends Thread{
    static  int count = 0;

    @Override
    public void run() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (SynchronizedThread.class){
            count++;
        }

    }
}

/**
 * 保证原子操作,比synchronized速度快
 */
class AtomicIntegerThread extends Thread{

    static final AtomicInteger at=new AtomicInteger();

    @Override
    public void run() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        at.incrementAndGet();

    }

}

class AtomicReferenceThread{

}

class Count{
    public int count;
    public Count(int count)
    {
        this.count=count;
    }
    public String toString()
    {
        return "这个对象的位置是："+count;
    }
}
