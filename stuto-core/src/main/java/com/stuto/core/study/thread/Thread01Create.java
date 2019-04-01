package com.stuto.core.study.thread;

/**
 * 多线程学习第一课
 * 参考网址:<a>https://blog.csdn.net/qq_34337272/article/details/79640870</a>
 * @author 作者 : zyq
 * 创建时间：2019/2/24
 *  @version 0.0.1
 */

public class Thread01Create {

    public static void main(String[] args) throws InterruptedException {
        SharedVariableThread sharedVariableThread = new SharedVariableThread();
        // 下列线程都是通过sharedVariableThread对象创建的
//        Thread a = new Thread(sharedVariableThread,"A");
//        Thread b = new Thread(sharedVariableThread,"B");
//        Thread c = new Thread(sharedVariableThread,"C");
//        Thread d = new Thread(sharedVariableThread,"D");
//        Thread e = new Thread(sharedVariableThread,"E");
//
//        a.start();
//        b.start();
//        c.start();
//        d.start();
//        e.start();

        /**
         * 测试线程中断
         */
       /* Thread interrunptThread = new InterruptThread();
        interrunptThread.start();

        Thread.sleep(2000);
        interrunptThread.interrupt();
        Thread.currentThread().interrupt();
        System.out.println("判断中断状态:"+ InterruptThread.interrupted());
        System.out.println("判断中断状态:"+ interrunptThread.isInterrupted());
        System.out.println("判断中断状态:"+ interrunptThread.isInterrupted());

        System.out.println("判断中断状态:"+ interrunptThread.isInterrupted());*/

        /**
         * 测试线程优先级
         */
        /*PriorityThread priorityThread1 = new PriorityThread();
        priorityThread1.setName("A");
        priorityThread1.setPriority(Thread.MAX_PRIORITY);
        PriorityThread priorityThread2 = new PriorityThread();
        priorityThread2.setName("B");
        priorityThread2.setPriority(Thread.MIN_PRIORITY);
        priorityThread1.start();
        priorityThread2.start();*/

        /**
         * 测试守护线程
         */
        DaemonThread daemonThread = new DaemonThread();
        daemonThread.setDaemon(true);
        daemonThread.start();
        Thread.sleep(1000);

    }
}


/**
 * 线程间共享变量,如果不加同步关键字或其它解决办法,数据可能出错
 */
class SharedVariableThread extends Thread{

    private int count = 5;

    @Override
    public void run() {
        super.run();
        count--;
        System.out.println(SharedVariableThread.currentThread().getName()+" 计算,count=" + count);
    }
}

/**
 *  线程中断
 *  interrupted()：测试当前线程是否已经是中断状态，执行后具有将中断状态标志清除为false的功能,
 *      是静态方法,通过类调用,不管是通过哪个类调用,都是获取当前执行线程的中断状态
 *  isInterrupted()： 测试线程Thread对相关是否已经是中断状态，但部清楚状态标志
 *
 */
class InterruptThread extends Thread{

    @Override
    public void run() {
        super.run();
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        for (int i = 0; i < 5000000; i++) {
            System.out.println(1111);
            if((InterruptThread.interrupted())){
                System.out.println("已经是停止状态了!我要退出了!");
                break;
            }
//            System.out.println( "i=" + i );

        }
        System.out.println("看到这句话说明线程并未终止------");

    }
}

/**
 * 优先级数字越大,越优先
 */
class PriorityThread extends Thread{
    private int count = 500000;
    @Override
    public void run() {
        super.run();
        while (true){
            count--;
            System.out.println(Thread.currentThread().getName()+":"+Thread.currentThread().getPriority()+":"+count);
        }

    }
}

/**
 * 守护进程,守护进程随着主进程的关闭自动关闭
 */
class DaemonThread extends Thread{
    private int count = 0;

    @Override
    public void run() {
        super.run();
        while (true){
            count++;
            System.out.println(count);
        }

    }
}
