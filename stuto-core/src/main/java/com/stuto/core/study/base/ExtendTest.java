package com.stuto.core.study.base;

/**
 * @author 作者 : zyq
 * 创建时间：2019/3/25 10:46
 * @version 0.0.1
 */
public class ExtendTest {
    protected String a = "1";
    protected void printA(){
        System.out.println(a);
    }

    public static void main(String[] args) {
        Sub sub = new Sub();
        sub.printA();
    }

}


class Sub extends ExtendTest{

    protected String a= "2";


}
