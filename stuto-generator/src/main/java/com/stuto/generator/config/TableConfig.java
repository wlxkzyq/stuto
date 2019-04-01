package com.stuto.generator.config;

/**
 * @author 作者 : zyq
 * 创建时间：2019/3/22 16:25
 * @version 0.0.1
 */
public class TableConfig {

    /**
     * 模块,指哪个模块的业务表
     * 比如:订单模块order
     */
    private String module="";


    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
