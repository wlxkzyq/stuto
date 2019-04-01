package com.stuto.generator.generators.tw;

import com.stuto.generator.api.IntrospectedTable;
import com.stuto.generator.api.dom.java.FullyQualifiedJavaType;
import com.stuto.generator.api.dom.java.JavaVisibility;
import com.stuto.generator.api.dom.java.Method;
import com.stuto.generator.api.dom.java.Parameter;
import com.stuto.generator.generators.MapperGenerator;

/**
 * @author yongqiang.zhang
 * @version 1.0 , 2018/11/16 17:34
 */
public class MapperGeneratorTw extends MapperGenerator {

    protected final String SQL_BUILDER_EXTEND_TYPE = "com.el.core.jdbc.Sql";


    public MapperGeneratorTw() {
    }

    public MapperGeneratorTw(IntrospectedTable table) {
        super(table);
    }


    @Override
    protected void init() {
        super.init();
        super.SQL_BUILDER_EXTEND_TYPE = this.SQL_BUILDER_EXTEND_TYPE;
        if ("id".equals(super.keyColumn) && "java.lang.Integer".equals(super.keyType)) {
            super.keyType = "java.lang.Long";
            super.javaKeyType = new FullyQualifiedJavaType(keyType);
        }
    }

    /**
     * 生成mapper delete 接口方法
     *
     * @return delete method
     */
    @Override
    protected Method buildDeleteMethod() {
        Method method = super.buildDeleteMethod();
        if ("java.lang.Long".equals(keyType) && "id".equals(super.keyColumn)) {
            Parameter parameter1 = new Parameter(FullyQualifiedJavaType.getNewListInstance(keyType), "keys");
            parameter1.addAnnotation("@Param(\"keys\")");
            method.getParameters().set(0, parameter1);
        }
        return method;
    }

    /**
     * 生成mapper logicalDelete 接口方法
     *
     * @return logicalDelete method
     */
    @Override
    protected Method buildLogicalDeleteMethod() {
        Method method = super.buildLogicalDeleteMethod();
        if ("java.lang.Long".equals(keyType) && "id".equals(super.keyColumn)) {
            Parameter parameter1 = new Parameter(FullyQualifiedJavaType.getNewListInstance(keyType), "keys");
            parameter1.addAnnotation("@Param(\"keys\")");
            method.getParameters().set(0, parameter1);
        }
        return method;
    }


    /**
     * 生成SqlBuilder类的delete方法,删除方法跟父类不同的是使用list作为参数
     *
     * @return delete method
     */
    @Override
    protected Method generateDeleteBuilderMethod() {
        if ("java.lang.Long".equals(keyType) && "id".equals(super.keyColumn)) {
            Method method = new Method(DELETE);
            method.setReturnType(FullyQualifiedJavaType.getStringInstance());
            method.setVisibility(JavaVisibility.PUBLIC);
            Parameter parameter1 = new Parameter(FullyQualifiedJavaType.getNewListInstance(keyType), "keys");
            parameter1.addAnnotation("@Param(\"keys\")");
            method.addParameter(parameter1);
            method.addBodyLine("DELETE_FROM(TABLE_NAME);");
            method.addBodyLine("WHERE(\"" + keyColumn + " in \" + SqlUtil.toSqlNumberSet(keys));");
            FullyQualifiedJavaType sqlUtilType = new FullyQualifiedJavaType("com.el.core.util.SqlUtil");
            topLevelClass.addImportedType(sqlUtilType);
            method.addBodyLine("return toString();");
            return method;
        }

        return super.generateDeleteBuilderMethod();
    }

    /**
     * 生成SqlBuilder类的logicalDelete方法
     *
     * @return logicalDelete method
     */
    @Override
    protected Method generateLogicalDeleteBuilderMethod() {
        if ("java.lang.Long".equals(keyType) && "id".equals(super.keyColumn)) {
            Method method = new Method(LOGICAL_DELETE);
            method.setReturnType(FullyQualifiedJavaType.getStringInstance());
            method.setVisibility(JavaVisibility.PUBLIC);
            Parameter parameter1 = new Parameter(FullyQualifiedJavaType.getNewListInstance(keyType), "keys");
            parameter1.addAnnotation("@Param(\"keys\")");
            method.addParameter(parameter1);
            method.addBodyLine("UPDATE(TABLE_NAME);");
            method.addBodyLine("SET(\"" + delFlagColumn + " = 1\");");
            method.addBodyLine("WHERE(\"" + keyColumn + " in \" + SqlUtil.toSqlNumberSet(keys));");
            FullyQualifiedJavaType sqlUtilType = new FullyQualifiedJavaType("com.el.core.util.SqlUtil");
            topLevelClass.addImportedType(sqlUtilType);
            method.addBodyLine("return toString();");
            return method;
        }

        return super.generateLogicalDeleteBuilderMethod();
    }

    /**
     * 生成SqlBuilder类的SelectByCondition方法
     *
     * @return SelectByCondition method
     */
    @Override
    protected Method generateSelectByConditionBuilderMethod() {
        Method selectByConditionMethod = new Method(SELECT_BY_CONDITION);
        selectByConditionMethod.setReturnType(FullyQualifiedJavaType.getStringInstance());
        selectByConditionMethod.setVisibility(JavaVisibility.PUBLIC);
        Parameter parameter1 = new Parameter(queryType);
        selectByConditionMethod.addParameter(parameter1);
        selectByConditionMethod.addBodyLine(SELECT_FIELDS + "();");
        selectByConditionMethod.addBodyLine(FROM_AND_WHERE + "(" + parameter1.getName() + ");");

        selectByConditionMethod.addBodyLine("return "+parameter1.getName()+".getLimit()==0?toString():toPagingSql("+parameter1.getName()+");");

        return selectByConditionMethod;
    }
}
