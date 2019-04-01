package com.stuto.core.pub;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 货币
 * @author yongqiang.zhang
 * @version 1.0 , 2019/1/9 18:51
 */
public enum CurrEnum {
    GENERAL("¤","通用"),
    CNY("￥","人民币"),
    USD("$","美元"),
    EUR("€", "欧元"),
    JPY("¥","日元"),

    ;


    CurrEnum(String symbol, String desc) {
        this.symbol = symbol;
        this.desc = desc;
    }

    /**
     * 货币符号
     */
    private String symbol;

    /**
     * 货币描述
     */
    private String desc;

    public static String transferCurr(BigDecimal amt,String curr){
        if (amt == null) {
            amt = BigDecimal.ZERO;
        }
        if(curr ==null){
            curr = "GENERAL";
        }
        String currAmt = null;
        try {
            currAmt = CurrEnum.valueOf(curr).symbol + amt.setScale(2, RoundingMode.HALF_UP);
        } catch (IllegalArgumentException e) {
            currAmt = CurrEnum.valueOf("GENERAL").symbol + amt.setScale(2, RoundingMode.HALF_UP);
        }
        return currAmt;
    }

}
