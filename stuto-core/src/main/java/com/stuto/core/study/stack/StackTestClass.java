package com.stuto.core.study.stack;

/**
 * 栈的学习测试
 * @author 作者 : zyq
 * 创建时间：2019/2/27 23:09
 * @version 0.0.1
 */
public class StackTestClass {
    int i = 0;

    public static void method1(){
        System.out.println("method1");
        method2();
    }

    public static void method2(){
        System.out.println("method2");
    }

    public static void main(String[] args) {
        System.out.println("main method");
        StackTestClass stackTestClass = new StackTestClass();
        method1();
        StackTestClass stackTestClass2 = new StackTestClass();

    }
}
