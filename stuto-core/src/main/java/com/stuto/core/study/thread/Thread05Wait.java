package com.stuto.core.study.thread;

import java.util.ArrayList;
import java.util.List;

/**
 * wait/notify(等待通知机制)
 *
 * @author 作者 : zyq
 * 创建时间：2019/2/24 19:18
 * @version 0.0.1
 */
public class Thread05Wait {

    public static void main(String[] args) {
        try {
            Object lock = new Object();

            ThreadA a = new ThreadA(lock);
            a.start();

            Thread.sleep(50);

            ThreadB b = new ThreadB(lock);
            b.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyList {
    private static List<String> list = new ArrayList<>();

    public static void add() {
        list.add("anyString");
    }

    public static int size() {
        return list.size();
    }

}

class ThreadA extends Thread {

    private Object lock;

    public ThreadA(Object lock) {
        super();
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            synchronized (lock) {
                if (MyList.size() != 5) {
                    System.out.println("wait begin "
                        + System.currentTimeMillis());
                    lock.wait();
                    System.out.println("wait end  "
                        + System.currentTimeMillis());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}


class ThreadB extends Thread {
    private Object lock;

    public ThreadB(Object lock) {
        super();
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            synchronized (lock) {
                for (int i = 0; i < 10; i++) {
                    MyList.add();
                    if (MyList.size() == 5) {


                    }
                    System.out.println("添加了" + (i + 1) + "个元素!");
                    Thread.sleep(1000);
                }
                lock.notify();
                System.out.println("已发出通知！");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
