package com.stuto.generator.internal;

import com.alibaba.fastjson.JSONObject;
import com.stuto.core.pub.StringUtil;
import com.stuto.generator.api.IntrospectedTable;
import com.stuto.generator.config.GeneratorContext;
import com.stuto.generator.generators.util.ConfigUtil;

/**
 * 类目处理器
 * @author 作者 : zyq
 * 创建时间：2019/3/25 15:53
 * @version 0.0.1
 */
public interface ClassNameHandler {
    /**
     * 处理类的包名
     *
     * @return 包名:rootPackage+module名+generator的package
     */
    public String wrapClassPackage(IntrospectedTable introspectedTable, String generatorName);

    /**
     * 封装类名,不包含路径的简单类名
     *
     * @return
     */
    public String wrapClassName(IntrospectedTable introspectedTable);

    /**
     * 封装类名,返回的类名应当包含完整的包名路径
     *
     * @return
     */
    public String wrapFullClassName(IntrospectedTable introspectedTable, String generatorName);

}
