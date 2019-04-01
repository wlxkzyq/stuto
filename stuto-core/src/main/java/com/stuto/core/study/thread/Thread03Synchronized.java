package com.stuto.core.study.thread;

/**
 * synchronized 关键字理解,可以修饰方法和代码块
 * 是重入锁,悲观锁
 * @author 作者 : zyq
 * 创建时间：2019/2/24 18:10
 * @version 0.0.1
 */
public class Thread03Synchronized {

    private int oount = 1;
}

/**
 * 直接synchronized方法，能解决多线程问题，但是速度较慢
 */
class Task{
    private String getData1;
    private String getData2;


    public synchronized  void doLongTimeTask() {
        try {
            System.out.println("begin task");
            Thread.sleep(3000);
            getData1 = "长时间处理任务后从远程返回的值1 threadName="
                + Thread.currentThread().getName();
            getData2 = "长时间处理任务后从远程返回的值2 threadName="
                + Thread.currentThread().getName();
            System.out.println(getData1);
            System.out.println(getData2);
            System.out.println("end task");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 使用synchronized代码块，提高速度
 * 主要是将浪费时间的代码不要放在同步代码块里面，异步操作
 */
class Task2{
    private String getData1;
    private String getData2;


    public  void doLongTimeTask() {
        try {
            System.out.println("begin task");
            Thread.sleep(3000);

            String privateGetData1 = "长时间处理任务后从远程返回的值1 threadName="
                + Thread.currentThread().getName();
            String privateGetData2 = "长时间处理任务后从远程返回的值2 threadName="
                + Thread.currentThread().getName();

            synchronized (this) {
                getData1 = privateGetData1;
                getData2 = privateGetData2;
            }

            System.out.println(getData1);
            System.out.println(getData2);
            System.out.println("end task");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
