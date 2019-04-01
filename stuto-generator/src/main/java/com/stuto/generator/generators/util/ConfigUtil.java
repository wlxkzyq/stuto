package com.stuto.generator.generators.util;

import com.alibaba.fastjson.JSONObject;
import com.stuto.generator.config.GeneratorContext;

/**
 * @author 作者 : zyq
 * 创建时间：2019/3/25 15:28
 * @version 0.0.1
 */
public class ConfigUtil {

    public static JSONObject getGeneratorCfg(String generatorName){
        JSONObject jsonObject = GeneratorContext.cfgJson.getJSONObject("generators").
            getJSONObject("topClassGenerators").getJSONObject(generatorName);
        if(jsonObject==null){
            jsonObject = GeneratorContext.cfgJson.getJSONObject("generators").
                getJSONObject("templateGenerators").getJSONObject(generatorName);
        }
        return jsonObject;
    }
}
