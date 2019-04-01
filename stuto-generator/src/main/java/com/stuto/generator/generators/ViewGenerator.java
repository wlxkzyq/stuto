package com.stuto.generator.generators;

import com.alibaba.fastjson.JSONObject;
import com.stuto.core.pub.StringUtil;
import com.stuto.generator.api.IntrospectedColumn;
import com.stuto.generator.api.IntrospectedTable;
import com.stuto.generator.api.dom.java.Field;
import com.stuto.generator.api.dom.java.FullyQualifiedJavaType;
import com.stuto.generator.api.dom.java.JavaVisibility;
import com.stuto.generator.config.GeneratorContext;
import com.stuto.generator.generators.util.LombokUtil;
import com.stuto.generator.internal.DefaultTopLevelClassGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * view生成器
 * @author 作者 : zyq
 * 创建时间：2019/3/22 17:58
 * @version 0.0.1
 */
public class ViewGenerator extends DefaultTopLevelClassGenerator {
    public ViewGenerator(IntrospectedTable table) {
        this.introspectedTable = table;
    }

    protected static final String GENERATOR_NAME = "ViewGenerator";

    @Override
    protected void init() {
        super.generatorName=GENERATOR_NAME;
        super.init();
    }

    /**
     * 获取数据库列在java类种的类型
     *
     * @param column
     * @return
     */
    protected FullyQualifiedJavaType wrapFieldType(IntrospectedColumn column) {
        return new FullyQualifiedJavaType(
            GeneratorContext.fieldTypeMap.get(column.getTypeName().toLowerCase()));
    }

    /**
     * 设置字段注释
     *
     * @param field
     * @param column
     */
    protected void addFieldDoc(Field field, IntrospectedColumn column) {
        field.addJavaDocLine("/**");
        field.addJavaDocLine(" * " + column.getRemarks() + " 【" + introspectedTable.getTableName() + "." + column.getColumnName() + "】");
        field.addJavaDocLine(" */");
    }

    /**
     * 添加View类特有的注解
     */
    @Override
    protected void addClassAnnotation() {
        super.addClassAnnotation();
        String extendsString = generatorCfg.getString("extends");
        LombokUtil.addLombokEntityAnnotation(topLevelClass,!StringUtil.isBlank(extendsString));
    }

    @Override
    protected List<Field> buildFields() {
        List<Field> fields = new ArrayList<>();
        for (IntrospectedColumn column : introspectedTable.getColumns()) {
            // 设置字段类型
            FullyQualifiedJavaType columnJavaType = wrapFieldType(column);
            topLevelClass.addImportedType(columnJavaType);
            // 设置字段名称
            String fieldName = StringUtil.getCamelCaseString(column.getColumnName(), false);
            Field field = new Field(fieldName, columnJavaType);
            field.setVisibility(JavaVisibility.PRIVATE);
            //添加字段注释
            addFieldDoc(field, column);
            fields.add(field);
        }
        return fields;
    }

}
