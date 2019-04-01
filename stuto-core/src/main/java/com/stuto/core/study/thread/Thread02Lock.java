package com.stuto.core.study.thread;

/**
 * 多线程锁的学习
 * 参考网址<a>https://blog.csdn.net/qq_34337272/article/details/79655194</a>
 * <p>
 * 1.synchronized关键字取得的锁都是对象锁，而不是把一段代码或方法当做锁。如果多个线程访问的是同一个对象，
 * 哪个线程先执行带synchronized关键字的方法，则哪个线程就持有该方法，那么其他线程只能呈等待状态。
 * 如果多个线程访问的是多个对象则不一定，因为多个对象会产生多个锁。
 * <p>
 * 2.如果父类有一个带synchronized关键字的方法，子类继承并重写了这个方法。
 但是同步不能继承，所以还是需要在子类方法中添加synchronized关键字。
 *
 * @author 作者 : zyq
 * 创建时间：2019/2/24 17:15
 * @version 0.0.1
 */

public class Thread02Lock {
    Lock lock = new Lock();

    public void print() throws InterruptedException {
        lock.lock();
//        doAdd();
        lock.unLock();
    }

    public void doAdd() throws InterruptedException {
        lock.lock();
        //do something
        lock.unLock();
    }

    Lock2 lock2 = new Lock2();

    public void print2() throws InterruptedException {
        lock2.lock();
        System.out.println("做第一个事情");
        doAdd2();
        lock2.unLock();
    }

    public void doAdd2() throws InterruptedException {
        lock2.lock();
        System.out.println("做第二个些事情");
        lock2.unLock();
    }


    public static void main(String[] args) throws InterruptedException {
        /**
         * 测试不可重入锁
         */
        Thread02Lock thread02Lock = new Thread02Lock();
//        thread02Lock.print();

        /**
         * 测试可重入锁
         */
        thread02Lock.print2();


    }

}

/**
 * 不可重入锁，即当线程获取某一个代码的锁的时候，当前线程要第二次获取该锁会让其等待，获取不到锁叫不可重入锁
 */
class Lock {
    private boolean isLocked = false;

    public synchronized void lock() throws InterruptedException {
        while (isLocked) {
            System.out.println("在已经锁的情况下重新锁");
            wait();
        }
        isLocked = true;
        System.out.println("加锁");
    }

    public synchronized void unLock() {
        isLocked = false;
        System.out.println("解锁");
        notify();

    }
}

/**
 * 可重入锁
 */
class Lock2 {
    private boolean isLocked = false;
    Thread lockedBy = null;
    int lockedCount = 0;

    public synchronized void lock() throws InterruptedException {
        Thread thread = Thread.currentThread();
        while (isLocked && lockedBy != thread) {
            wait();
        }
        System.out.println("加锁");
        isLocked = true;
        lockedCount++;
        lockedBy = thread;

    }

    public synchronized void unLock() {
        if (Thread.currentThread() == this.lockedBy) {
            lockedCount--;
            System.out.println("解锁");
            if (lockedCount == 0) {
                isLocked = false;
                notify();
            }
        }
    }

}
