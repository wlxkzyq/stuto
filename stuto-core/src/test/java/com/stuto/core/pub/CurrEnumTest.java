package com.stuto.core.pub;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * @author yongqiang.zhang
 * @version 1.0 , 2019/1/9 19:25
 */
public class CurrEnumTest {
    @Test
    public void transferCurr() throws Exception {
        System.out.println(CurrEnum.transferCurr(BigDecimal.valueOf(2.25454),"JPY1"));
        System.out.println(CurrEnum.transferCurr(BigDecimal.ZERO,"CNY"));
        System.out.println(CurrEnum.transferCurr(BigDecimal.valueOf(2.25554),"USD"));
        System.out.println(CurrEnum.transferCurr(BigDecimal.valueOf(2.25454),"JPY"));
        System.out.println(CurrEnum.transferCurr(null,"JPY"));
        System.out.println(CurrEnum.transferCurr(null,null));

    }

}
