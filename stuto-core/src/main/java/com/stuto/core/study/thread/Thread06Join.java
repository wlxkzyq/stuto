package com.stuto.core.study.thread;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 管道的学习
 * 参考网址：<a>https://blog.csdn.net/qq_34337272/article/details/79694226</a>
 * @author 作者 : zyq
 * 创建时间：2019/2/24 20:38
 * @version 0.0.1
 */
public class Thread06Join {

    // Atomic integer containing the next thread ID to be assigned
    private static final AtomicInteger nextId = new AtomicInteger(0);

    private static final ThreadLocal<Integer> threadId = ThreadLocal.withInitial( () -> nextId.getAndIncrement() );

    private static final int aaa = 1;

    public static void main(String[] args) throws InterruptedException {

        /**
         * 测试线程间通信
         */
        /*try {
            WriteData writeData = new WriteData();
            ReadData readData = new ReadData();

            PipedInputStream inputStream = new PipedInputStream();
            PipedOutputStream outputStream = new PipedOutputStream();

            // inputStream.connect(outputStream);
            outputStream.connect(inputStream);

            ThreadRead threadRead = new ThreadRead(readData, inputStream);
            threadRead.start();

            Thread.sleep(2000);

            ThreadWrite threadWrite = new ThreadWrite(writeData, outputStream);
            threadWrite.start();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        /**
         * 测试join方法
         */
        /*System.out.println("主线程开始执行");
        ThreadJoin threadJoin = new ThreadJoin();
        System.out.println("子线程启动");
        threadJoin.start();
        System.out.println("子线程join");
//        等待子线程结束主线程执行
//        threadJoin.join();
//        只等待500毫秒
        threadJoin.join(500);
        System.out.println("主线程运行结束");*/

        /**
         * 测试ThreadLocal
         */
        /*System.out.println(threadId.get());
        threadId.set(2);
        System.out.println(threadId.get());*/
        ThreadLocalTest threadLocalTest = new ThreadLocalTest();
        threadLocalTest.start();
        Thread.sleep(1000);
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println("       在Main线程中取值=" + ThreadLocalTest.threadId.get());
                Thread.sleep(100);
            }
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }




    }

}




class WriteData {

    public void writeMethod(PipedOutputStream out) {
        try {
            System.out.println("write :");
            for (int i = 0; i < 300; i++) {
                String outData = "" + (i + 1);
                out.write(outData.getBytes());
                System.out.print(outData);
            }
            System.out.println();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
class ThreadWrite extends Thread{
    private WriteData write;
    private PipedOutputStream out;

    public ThreadWrite(WriteData write, PipedOutputStream out) {
        super();
        this.write = write;
        this.out = out;
    }

    @Override
    public void run() {
        write.writeMethod(out);
    }

}

class ReadData{
    public void readMethod(PipedInputStream input) {
        try {
            System.out.println("read  :");
            byte[] byteArray = new byte[20];
            int readLength = input.read(byteArray);
            while (readLength != -1) {
                String newData = new String(byteArray, 0, readLength);
                System.out.print("//"+newData);
                readLength = input.read(byteArray);
            }
            System.out.println();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ThreadRead extends Thread{
    private ReadData read;
    private PipedInputStream input;

    public ThreadRead(ReadData read, PipedInputStream input) {
        super();
        this.read = read;
        this.input = input;
    }

    @Override
    public void run() {
        read.readMethod(input);
    }
}


/**
 * join方法,主线程需要等待子线程执行完成之后再结束
 * Thread.sleep(2000)不会释放锁，threadTest.join(2000)会释放锁 。
 */
class ThreadJoin extends Thread{

    @Override
    public void run() {
        super.run();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("join的子线程,延迟1秒执行");
    }
}

class ThreadLocalTest extends Thread{
    // Atomic integer containing the next thread ID to be assigned
    private static final AtomicInteger nextId = new AtomicInteger(0);

    // Thread local variable containing each thread's ID
    static final ThreadLocal<Integer> threadId = ThreadLocal.withInitial( () -> nextId.incrementAndGet() );

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println("ThreadLocalTest=" + threadId.get());
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
