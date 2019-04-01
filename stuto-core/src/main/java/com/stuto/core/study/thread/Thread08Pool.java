package com.stuto.core.study.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池学习
 *
 *  <p>BlockingQueue 阻塞队列的可选情况</p>
 * {@link java.util.concurrent.ArrayBlockingQueue}
 * {@link java.util.concurrent.LinkedBlockingQueue}
 *
 * @author 作者 : zyq
 * 创建时间：2019/2/25 16:10
 * @version 0.0.1
 */
public class Thread08Pool {


    public static void main(String[] args) {

        /**
         * 一个阻塞队列,当队列为空，消费者线程被阻塞；当队列装满，生产者线程被阻塞；
         * {@code ArrayBlockingQueue}
         */
        BlockingQueue workQueue = new LinkedBlockingQueue();

        // 创建一个线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,
            15,5, TimeUnit.SECONDS,workQueue);

    }

}
