package com.stuto.core.pub;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author yongqiang.zhang
 * @version 1.0 , 2018/5/7 14:35
 */
public class StutoExceptionTest {
    @Test
    public void getMsg() throws Exception {
        StutoException e = new StutoException("junit test exception");
        assertEquals("junit test exception",e.getMsg());
    }

}