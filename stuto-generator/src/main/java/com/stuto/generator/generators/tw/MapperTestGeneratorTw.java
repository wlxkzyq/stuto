package com.stuto.generator.generators.tw;

import com.stuto.generator.api.IntrospectedTable;
import com.stuto.generator.api.dom.java.FullyQualifiedJavaType;
import com.stuto.generator.api.dom.java.Method;
import com.stuto.generator.generators.MapperTestGenerator;

/**
 * @author 作者 : zyq
 * 创建时间：2019/3/29 11:17
 * @version 0.0.1
 */
public class MapperTestGeneratorTw extends MapperTestGenerator {
    public MapperTestGeneratorTw(IntrospectedTable table) {
        super(table);
    }

    @Override
    protected void init() {
        super.init();
        if ("id".equals(super.keyColumn) && "java.lang.Integer".equals(super.keyType)) {
            super.keyType = "java.lang.Long";
            super.javaKeyType = new FullyQualifiedJavaType(super.keyType);
        }
    }


    /**
     * 生成mapperTest类的delete接口方法
     *
     * @return
     */
    protected Method buildDeleteMethod() {
        Method method = getTestMethod();
        method.setName(DELETE_NAME);
        addKeyBody(method);
        method.addBodyLine("int count = mapper."+DELETE_NAME+"(Arrays.asList(key));");
        method.addBodyLine("System.out.println(count);");
        topLevelClass.addImportedType("java.util.Arrays");
        return method;
    }

    /**
     * 生成mapperTest类的LogicalDelete接口方法
     *
     * @return
     */
    protected Method buildLogicalDeleteMethod() {
        Method method = getTestMethod();
        method.setName(LOGICAL_DELETE_NAME);
        addKeyBody(method);
        method.addBodyLine("int count = mapper."+LOGICAL_DELETE_NAME+"(Arrays.asList(key));");
        method.addBodyLine("System.out.println(count);");
        topLevelClass.addImportedType("java.util.Arrays");
        return method;
    }



}
