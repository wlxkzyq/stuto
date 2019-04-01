package com.stuto.generator.generators.tw;

import com.stuto.core.pub.StutoRuntimeException;
import com.stuto.generator.api.IntrospectedTable;
import com.stuto.generator.api.dom.java.FullyQualifiedJavaType;
import com.stuto.generator.internal.TemplateGenerator;

/**
 * @author 作者 : zyq
 * 创建时间：2019/4/1 10:25
 * @version 0.0.1
 */
public class TemplateGeneratorTw extends TemplateGenerator {

    public TemplateGeneratorTw(IntrospectedTable introspectedTable, String generatorName) {
        super(introspectedTable, generatorName);
    }

    @Override
    protected void init() throws StutoRuntimeException {
        super.init();
        if ("id".equals(super.keyColumn) && "java.lang.Integer".equals(super.keyType)) {
            super.keyType = "java.lang.Long";
            super.javaKeyType = new FullyQualifiedJavaType(keyType);
        }
    }
}
