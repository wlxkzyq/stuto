package com.stuto.generator.internal;

import com.alibaba.fastjson.JSONObject;
import com.stuto.generator.config.GeneratorContext;

/**
 * 生成器接口
 * @author yongqiang.zhang
 * @version 1.0 , 2018/11/16 17:38
 */
public interface Generator {

    JSONObject cfgJson = GeneratorContext.cfgJson;
    String getFormattedContent();
    void writeToFile();
}
