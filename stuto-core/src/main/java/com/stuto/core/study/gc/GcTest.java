package com.stuto.core.study.gc;

import java.util.Vector;

/**
 * 垃圾回收机制测试
 *
 * @author 作者 : zyq
 * 创建时间：2019/3/2 13:56
 * @version 0.0.1
 */
public class GcTest {

    private String name = "";
    static Vector<GcTest> v = new Vector<>();

    public static void main(String[] args) {
        // 这样写存在堆中的GcTest对象会被回收
        /*GcTest o = new GcTest();
        System.out.println(o);
        o = null;
        System.out.println(o);*/

        for (int i = 0; i < 100; i++) {
            GcTest gc = new GcTest();
            gc.name=""+i;
            v.add(gc);
            gc = null;
        }
        System.out.println(v);


    }

}
