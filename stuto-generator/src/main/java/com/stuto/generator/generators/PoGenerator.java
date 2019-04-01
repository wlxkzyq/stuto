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
 * 生成数据库字段对应的po实体类对象
 *
 * @author yongqiang.zhang
 * @version 1.0 , 2018/11/15 17:38
 */
public class PoGenerator extends DefaultTopLevelClassGenerator {

    protected static final String GENERATOR_NAME = "PoGenerator";

    public PoGenerator(IntrospectedTable table) {
        this.introspectedTable = table;
    }


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
     * 添加PO类特有的注解
     */
    @Override
    protected void addClassAnnotation() {
        super.addClassAnnotation();
        String extendsString = generatorCfg.getString("extends");
        LombokUtil.addLombokEntityAnnotation(topLevelClass,!StringUtil.isBlank(extendsString));
    }


    @Override
    protected List<Field> buildFields() {
        List<Field> poFields = new ArrayList<>();
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
            poFields.add(field);
        }
        return poFields;
    }


}
