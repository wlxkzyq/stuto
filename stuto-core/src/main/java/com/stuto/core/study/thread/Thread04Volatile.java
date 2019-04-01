package com.stuto.core.study.thread;

import java.time.LocalDateTime;

/**
 * volatile 关键字理解
 * 1. volatile让变量每次在使用的时候，都从主存中取。而不是从各个线程的“工作内存”。
 * 2. 当其它线程会随时更改这个变量时可以加改关键字
 * 3. 该关键字可以修饰基础数据类型
 * 4. 为什么不加volatile关键字加个输出语句或者sleep也能正常结束循环，是因为只要CPU有时间，JVM会尽力去保证变量值的更新。
 *      输出语句当cpu有时间去更新
 *
 *  AtomicInteger 原子整型
 * @author 作者 : zyq
 * 创建时间：2019/2/24 18:30
 * @version 0.0.1
 */
public class Thread04Volatile {

    public static void main(String[] args) throws InterruptedException {
        VolatileThread thread = new VolatileThread();

        System.out.println(LocalDateTime.now());
        thread.start();
        Thread.sleep(1000);
        thread.setRunning(false);
        System.out.println(LocalDateTime.now());
        System.out.println("已经赋值为false");

    }
}

class VolatileThread extends Thread{
    private volatile boolean isRunning = true;
    int m;

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }



    @Override
    public void run() {
        super.run();
        System.out.println("进入run了");
        while (isRunning == true) {
            int a=2;
            int b=3;
            int c=a+b;
            m=c;
//            System.out.println(m);
        }
        System.out.println(m);
        System.out.println("线程停止了");

    }
}
