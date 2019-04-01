package com.stuto.generator.generators;

import com.stuto.core.pub.StringUtil;
import com.stuto.core.pub.TimeUtil;
import com.stuto.generator.api.IntrospectedColumn;
import com.stuto.generator.api.IntrospectedTable;
import com.stuto.generator.api.dom.java.*;
import com.stuto.generator.config.GeneratorContext;
import com.stuto.generator.internal.DefaultTopLevelClassGenerator;
import com.stuto.generator.util.JavaBeanUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * mapper 生成器
 *
 * @author yongqiang.zhang
 * @version 1.0 , 2018/11/16 17:34
 */
public class MapperGenerator extends DefaultTopLevelClassGenerator {

    protected static final String GENERATOR_NAME = "MapperGenerator";
    protected static final String GENERATOR_NAME_PO = "PoGenerator";
    protected static final String GENERATOR_NAME_QUERY = "QueryGenerator";
    protected static final String GENERATOR_NAME_VIEW = "ViewGenerator";


    protected String delFlagColumn;

    protected final String INSERT_NAME = "insert";
    protected final String UPDATE_BY_KEY_NAME = "updateByKey";
    protected final String SQL_BUILDER_NAME = "SqlBuilder";
    protected String SQL_BUILDER_EXTEND_TYPE = "org.apache.ibatis.jdbc.AbstractSQL<" + SQL_BUILDER_NAME + ">";

    // selectFields 方法名
    protected final String SELECT_FIELDS = "selectFields";

    // fromAndWhere 方法名
    protected final String FROM_AND_WHERE = "fromAndWhere";

    protected final String SELECT_BY_CONDITION = "selectByCondition";
    // selectCountByCondition 方法名
    protected final String SELECT_COUNT_BY_CONDITION = "selectCountByCondition";

    // selectById 方法名
    protected final String SELECT_BY_KEY = "selectByKey";

    // 物理删除方法名
    protected final String DELETE = "delete";

    // 逻辑删除方法名
    protected final String LOGICAL_DELETE = "logicalDelete";
    protected String sqlBuilderInsertIgnore;
    protected String sqlBuilderUpdateIgnore;

    protected String keyColumn;
    protected String keyType;
    protected FullyQualifiedJavaType javaKeyType;

    protected FullyQualifiedJavaType poType;
    protected FullyQualifiedJavaType queryType;
    protected FullyQualifiedJavaType viewType;

    // myBatis 参数注解类
    protected final static FullyQualifiedJavaType mybatisParamType = new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param");

    // myBatis DeleteProvider 类
    FullyQualifiedJavaType deleteProviderType = new FullyQualifiedJavaType("org.apache.ibatis.annotations.DeleteProvider");

    // myBatis SelectKey 类
    FullyQualifiedJavaType insertSelectKeyType = new FullyQualifiedJavaType("org.apache.ibatis.annotations.SelectKey");

    //  myBatis UpdateProvider 类
    FullyQualifiedJavaType updateProviderType = new FullyQualifiedJavaType("org.apache.ibatis.annotations.UpdateProvider");

    // myBatis SelectProvider 类
    FullyQualifiedJavaType selectProviderType = new FullyQualifiedJavaType("org.apache.ibatis.annotations.SelectProvider");


    public MapperGenerator() {
    }

    public MapperGenerator(IntrospectedTable table) {
        this.introspectedTable =table;
    }


    @Override
    protected void init() {
        super.generatorName = GENERATOR_NAME;
        this.delFlagColumn = cfgJson.getJSONObject("db").getJSONObject("rules").getString("delFlagColumn");
        super.init();
        poType = new FullyQualifiedJavaType(wrapPoClassName());
        queryType = new FullyQualifiedJavaType(wrapQueryClassName());
        viewType = new FullyQualifiedJavaType(wrapViewClassName());
        sqlBuilderInsertIgnore = generatorCfg.getString("sqlBuilderInsertIgnore");
        sqlBuilderUpdateIgnore = generatorCfg.getString("sqlBuilderUpdateIgnore");
        IntrospectedColumn primaryKeyColumn = introspectedTable.getFirstPrimaryKeyColumn();
        if (primaryKeyColumn != null) {
            keyColumn = primaryKeyColumn.getColumnName();
            keyType = GeneratorContext.fieldTypeMap.get(primaryKeyColumn.getTypeName().toLowerCase());
            javaKeyType = new FullyQualifiedJavaType(keyType);
        }

    }

    /**
     * po类的全名
     *
     * @return
     */
    String wrapPoClassName() {
        return classNameHandler.wrapFullClassName(introspectedTable, GENERATOR_NAME_PO);
    }

    /**
     * query类的全名
     *
     * @return
     */
    String wrapQueryClassName() {
        return classNameHandler.wrapFullClassName(introspectedTable, GENERATOR_NAME_QUERY);
    }

    /**
     * query类的全名
     *
     * @return
     */
    String wrapViewClassName() {
        return classNameHandler.wrapFullClassName(introspectedTable, GENERATOR_NAME_VIEW);
    }


    protected void addClassDoc() {
        // 添加类注释
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * " + introspectedTable.getRemarks() + " Mapper");
        topLevelClass.addJavaDocLine(" * @author " + cfgJson.getJSONObject("individual").getString("name"));
        topLevelClass.addJavaDocLine(" * @email " + cfgJson.getJSONObject("individual").getString("email"));
        topLevelClass.addJavaDocLine(" * @date " + TimeUtil.dateToYmd(LocalDate.now()));
        topLevelClass.addJavaDocLine(" */");
    }

    @Override
    protected void buildClass() {
        topLevelClass.addInnerClass(buildSqlBuilder());
        topLevelClass.setJavaInterface(true);
        super.buildClass();

    }

    @Override
    protected List<Method> buildMethods() {
        List<Method> methods = new ArrayList<>();
        methods.add(buildInsertMethod());
        methods.add(buildUpdateByIdMethod());
        methods.add(buildSelectByConditionMethod());
        methods.add(buildSelectCountByConditionMethod());
        methods.add(buildSelectByKeyMethod());
        methods.add(buildDeleteMethod());
        methods.add(buildLogicalDeleteMethod());

        return methods;
    }

    /**
     * 生成mapper类的insert接口方法
     *
     * @return
     */
    protected Method buildInsertMethod() {
        Method insertMethod = new Method(INSERT_NAME);
        insertMethod.setReturnType(FullyQualifiedJavaType.getIntInstance());
        insertMethod.setVisibility(JavaVisibility.PUBLIC);

        Parameter insertMethodParam = new Parameter(poType);
        topLevelClass.addImportedType(poType);
        insertMethod.addParameter(insertMethodParam);
        String insertProviderAnnotation = "@InsertProvider(type = SqlBuilder.class, method = \""
            + INSERT_NAME + "\")";
        FullyQualifiedJavaType insertProviderType = new FullyQualifiedJavaType("org.apache.ibatis.annotations.InsertProvider");
        topLevelClass.addImportedType(insertProviderType);
        insertMethod.addAnnotation(insertProviderAnnotation);
        // 如果id 是主键,生成mysql的 @SelectKey 注解
        if ("id".equals(keyColumn.toLowerCase())) {
            String insertSelectKeyAnnotation = "@SelectKey(statement = \"SELECT LAST_INSERT_ID()\", keyProperty = \""
                + keyColumn + "\", before = false, resultType = " + keyType + ".class)";
            topLevelClass.addImportedType(insertSelectKeyType);
            insertMethod.addAnnotation(insertSelectKeyAnnotation);
        }
        return insertMethod;
    }

    /**
     * 生成mapper 的updateById 接口方法
     *
     * @return updateById method
     */
    protected Method buildUpdateByIdMethod() {
        Method updateByIdMethod = new Method(UPDATE_BY_KEY_NAME);
        updateByIdMethod.setReturnType(FullyQualifiedJavaType.getIntInstance());
        updateByIdMethod.setVisibility(JavaVisibility.PUBLIC);

        Parameter updateByIdMethodParam = new Parameter(poType);
        topLevelClass.addImportedType(poType);
        updateByIdMethod.addParameter(updateByIdMethodParam);
        String updateProviderAnnotation = "@UpdateProvider(type = SqlBuilder.class, method = \""
            + UPDATE_BY_KEY_NAME + "\")";
        topLevelClass.addImportedType(updateProviderType);
        updateByIdMethod.addAnnotation(updateProviderAnnotation);

        return updateByIdMethod;
    }

    /**
     * 生成mapper selectByCondition 接口方法
     *
     * @return selectByCondition method
     */
    protected Method buildSelectByConditionMethod() {
        Method selectByConditionMethod = new Method(SELECT_BY_CONDITION);
        selectByConditionMethod.setReturnType(FullyQualifiedJavaType.getNewListInstance(viewType.getShortName()));
        topLevelClass.addImportedType(FullyQualifiedJavaType.getNewListInstance());
        selectByConditionMethod.setVisibility(JavaVisibility.PUBLIC);

        Parameter selectByConditionMethodParam = new Parameter(queryType);
        topLevelClass.addImportedType(viewType);
        topLevelClass.addImportedType(queryType);
        selectByConditionMethod.addParameter(selectByConditionMethodParam);
        String selectProviderAnnotation = "@SelectProvider(type = SqlBuilder.class, method = \""
            + SELECT_BY_CONDITION + "\")";
        topLevelClass.addImportedType(selectProviderType);
        selectByConditionMethod.addAnnotation(selectProviderAnnotation);
        return selectByConditionMethod;
    }

    /**
     * 生成mapper SelectCountByCondition 接口方法
     *
     * @return selectByCondition method
     */
    protected Method buildSelectCountByConditionMethod() {
        Method method = new Method(SELECT_COUNT_BY_CONDITION);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setVisibility(JavaVisibility.PUBLIC);

        Parameter parameter1 = new Parameter(queryType);
        topLevelClass.addImportedType(queryType);
        method.addParameter(parameter1);
        String selectProviderAnnotation = "@SelectProvider(type = SqlBuilder.class, method = \""
            + SELECT_COUNT_BY_CONDITION + "\")";
        topLevelClass.addImportedType(selectProviderType);
        method.addAnnotation(selectProviderAnnotation);
        return method;
    }

    /**
     * 生成mapper selectByKey 接口方法
     *
     * @return selectByCondition method
     */
    protected Method buildSelectByKeyMethod() {
        Method method = new Method(SELECT_BY_KEY);
        method.setReturnType(viewType);
        method.setVisibility(JavaVisibility.PUBLIC);

        Parameter parameter1 = new Parameter(javaKeyType,"key");
        parameter1.addAnnotation("@Param(\"key\")");
        topLevelClass.addImportedType(viewType);
        topLevelClass.addImportedType(javaKeyType);
        topLevelClass.addImportedType(mybatisParamType);
        method.addParameter(parameter1);
        String selectProviderAnnotation = "@SelectProvider(type = SqlBuilder.class, method = \""
            + SELECT_BY_KEY + "\")";
        FullyQualifiedJavaType selectProviderType = new FullyQualifiedJavaType("org.apache.ibatis.annotations.SelectProvider");
        topLevelClass.addImportedType(selectProviderType);
        method.addAnnotation(selectProviderAnnotation);
        return method;
    }

    /**
     * 生成mapper delete 接口方法
     *
     * @return delete method
     */
    protected Method buildDeleteMethod() {
        Method deleteMethod = new Method(DELETE);
        deleteMethod.setReturnType(FullyQualifiedJavaType.getIntInstance());
        deleteMethod.setVisibility(JavaVisibility.PUBLIC);
        Parameter deleteMethodParam = new Parameter(javaKeyType, "key");
        deleteMethodParam.addAnnotation("@Param(\"key\")");
        deleteMethod.addParameter(deleteMethodParam);

        topLevelClass.addImportedType(mybatisParamType);

        topLevelClass.addImportedType(javaKeyType);
        String deleteProviderAnnotation = "@DeleteProvider(type = SqlBuilder.class, method = \""
            + DELETE + "\")";
        topLevelClass.addImportedType(deleteProviderType);
        deleteMethod.addAnnotation(deleteProviderAnnotation);

        return deleteMethod;
    }

    /**
     * 生成mapper logicalDelete 接口方法
     *
     * @return logicalDelete method
     */
    protected Method buildLogicalDeleteMethod() {
        Method logicalDeleteMethod = new Method(LOGICAL_DELETE);
        logicalDeleteMethod.setReturnType(FullyQualifiedJavaType.getIntInstance());
        logicalDeleteMethod.setVisibility(JavaVisibility.PUBLIC);

        Parameter deleteMethodParam = new Parameter(javaKeyType, "key");
        deleteMethodParam.addAnnotation("@Param(\"key\")");
        logicalDeleteMethod.addParameter(deleteMethodParam);
        topLevelClass.addImportedType(mybatisParamType);

        topLevelClass.addImportedType(javaKeyType);
        String annotation = "@UpdateProvider(type = SqlBuilder.class, method = \""
            + LOGICAL_DELETE + "\")";
        topLevelClass.addImportedType(updateProviderType);
        logicalDeleteMethod.addAnnotation(annotation);

        return logicalDeleteMethod;
    }

    /**
     * 生成builder 类
     *
     * @return
     */
    protected InnerClass buildSqlBuilder() {
        // 内部类定义
        InnerClass sqlBuilderClass = new InnerClass(SQL_BUILDER_NAME);
        FullyQualifiedJavaType sqlBuilderExtendType = new FullyQualifiedJavaType(SQL_BUILDER_EXTEND_TYPE);
        sqlBuilderClass.setSuperClass(sqlBuilderExtendType);
        topLevelClass.addImportedType(sqlBuilderExtendType);
        // 添加TABLE_NAME字段
        Field tableNameField = new Field("TABLE_NAME", FullyQualifiedJavaType.getStringInstance());
        tableNameField.setFinal(true);
        tableNameField.setStatic(true);
        tableNameField.setVisibility(JavaVisibility.PRIVATE);
        tableNameField.setInitializationString("\"" + introspectedTable.getTableName().toUpperCase() + "\"");
        sqlBuilderClass.addField(tableNameField);

        // 内部类添加getSelf方法
        if(SQL_BUILDER_EXTEND_TYPE.startsWith("org.apache.ibatis.jdbc.AbstractSQL<")){
            sqlBuilderClass.addMethod(generateGetSelfBuilderMethod());
        }


        // 内部类添加insert方法
        sqlBuilderClass.addMethod(generateInsertBuilderMethod());

        // 内部类添加updateById方法
        sqlBuilderClass.addMethod(generateUpdateByIdBuilderMethod());

        // 内部类添加 selectFields方法
        sqlBuilderClass.addMethod(generateSelectFieldsBuilderMethod());

        // 内部类添加 FromAndWhere方法
        sqlBuilderClass.addMethod(generateFromAndWhereBuilderMethod());

        // 内部类添加selectByConditioin方法
        sqlBuilderClass.addMethod(generateSelectByConditionBuilderMethod());

        // 内部类添加selectCountByConditioin方法
        sqlBuilderClass.addMethod(generateSelectCountByConditionBuilderMethod());

        // 内部类添加SelectByKey方法
        sqlBuilderClass.addMethod(generateSelectByKeyBuilderMethod());
        // 内部类添加delete方法
        sqlBuilderClass.addMethod(generateDeleteBuilderMethod());
        // 内部类添加logicalDelete方法
        sqlBuilderClass.addMethod(generateLogicalDeleteBuilderMethod());
        return sqlBuilderClass;
    }

    /**
     * 生成SqlBuilder类的insert方法
     *
     * @return insert method
     */
    protected Method generateGetSelfBuilderMethod() {
        Method method = new Method("getSelf");
        method.setReturnType(new FullyQualifiedJavaType(SQL_BUILDER_NAME));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addBodyLine("return this;");
        method.addAnnotation("@Override");

        return method;
    }

    /**
     * 生成SqlBuilder类的insert方法
     *
     * @return insert method
     */
    protected Method generateInsertBuilderMethod() {
        Method insertMethod = new Method(INSERT_NAME);
        insertMethod.setReturnType(FullyQualifiedJavaType.getStringInstance());
        insertMethod.setVisibility(JavaVisibility.PUBLIC);
        Parameter parameter1 = new Parameter(poType);
        insertMethod.addParameter(parameter1);
        insertMethod.addBodyLine("INSERT_INTO(TABLE_NAME);");
        insertMethod.addBodyLine("INTO_COLUMNS(\"" + delFlagColumn + "\");");
        insertMethod.addBodyLine("INTO_VALUES(\"0\");");
        for (IntrospectedColumn introspectedColumn : introspectedTable.getColumns()) {
            String column = introspectedColumn.getColumnName();
            if (sqlBuilderInsertIgnore.indexOf(column) > -1) {
                continue;
            }
            insertMethod.addBodyLine("INTO_COLUMNS(\"" + column + "\");");
            insertMethod.addBodyLine("INTO_VALUES(\"#{" + StringUtil.getCamelCaseString(column, false) + "}\");");
        }

        insertMethod.addBodyLine("return toString();");

        return insertMethod;
    }

    /**
     * 生成SqlBuilder类的update方法
     *
     * @return generateUpdateByIdBuilderMethod methond
     */
    protected Method generateUpdateByIdBuilderMethod() {
        Method updateByIdMethod = new Method(UPDATE_BY_KEY_NAME);
        updateByIdMethod.setReturnType(FullyQualifiedJavaType.getStringInstance());
        updateByIdMethod.setVisibility(JavaVisibility.PUBLIC);
        Parameter parameter1 = new Parameter(poType);
        updateByIdMethod.addParameter(parameter1);

        updateByIdMethod.addBodyLine("UPDATE(TABLE_NAME);");
        for (IntrospectedColumn introspectedColumn : introspectedTable.getColumns()) {
            String column = introspectedColumn.getColumnName();
            if (column.equals(keyColumn) || sqlBuilderUpdateIgnore.indexOf(column) > -1) {
                continue;
            }
            updateByIdMethod.addBodyLine("SET(\"" + column + " = #{" + StringUtil.getCamelCaseString(column, false) + "}\");");
        }

        updateByIdMethod.addBodyLine("WHERE(\"" + keyColumn + " = #{" + StringUtil.getCamelCaseString(keyColumn, false) + "}\");");
        updateByIdMethod.addBodyLine("return toString();");

        return updateByIdMethod;
    }

    /**
     * 生成SqlBuilder类的SelectFields方法
     *
     * @return SelectByCondition method
     */
    protected Method generateSelectFieldsBuilderMethod() {
        Method method = new Method(SELECT_FIELDS);
        method.setReturnType(null);
        method.setVisibility(JavaVisibility.PRIVATE);

        for (IntrospectedColumn introspectedColumn : introspectedTable.getColumns()) {
            String column = introspectedColumn.getColumnName();
            method.addBodyLine("SELECT(\"t1." + column + " as " + StringUtil.getCamelCaseString(column, false) + "\");");
        }

        return method;
    }

    /**
     * 生成SqlBuilder类的FromAndWhere方法
     *
     * @return SelectByCondition method
     */
    protected Method generateFromAndWhereBuilderMethod() {
        Method method = new Method(FROM_AND_WHERE);
        method.setReturnType(null);
        method.setVisibility(JavaVisibility.PRIVATE);

        Parameter parameter1 = new Parameter(queryType);
        method.addParameter(parameter1);

        String parameterName = queryType.getShortName().substring(0, 1).toLowerCase() + queryType.getShortName().substring(1);

        method.addBodyLine("FROM(TABLE_NAME+\" t1\");");
        method.addBodyLine("WHERE(\"t1." + delFlagColumn + " = 0\");");
        for (IntrospectedColumn introspectedColumn : introspectedTable.getColumns()) {
            String column = introspectedColumn.getColumnName();
            String property = StringUtil.getCamelCaseString(column, false);
            method.addBodyLine("if( " + parameterName + "." + JavaBeanUtil.getGetterMethodName(property) + "() != null ){");
            method.addBodyLine("WHERE(\"t1." + column + " = #{" + property + "}\");");
            method.addBodyLine("}");
        }

        return method;
    }

    /**
     * 生成SqlBuilder类的SelectByCondition方法
     *
     * @return SelectByCondition method
     */
    protected Method generateSelectByConditionBuilderMethod() {
        Method selectByConditionMethod = new Method(SELECT_BY_CONDITION);
        selectByConditionMethod.setReturnType(FullyQualifiedJavaType.getStringInstance());
        selectByConditionMethod.setVisibility(JavaVisibility.PUBLIC);
        Parameter parameter1 = new Parameter(queryType);
        selectByConditionMethod.addParameter(parameter1);
        selectByConditionMethod.addBodyLine(SELECT_FIELDS + "();");
        selectByConditionMethod.addBodyLine(FROM_AND_WHERE + "(" + parameter1.getName() + ");");

        selectByConditionMethod.addBodyLine("return toString();");

        return selectByConditionMethod;
    }

    /**
     * 生成SqlBuilder类的SelectCountByCondition方法
     *
     * @return SelectByCondition method
     */
    protected Method generateSelectCountByConditionBuilderMethod() {
        Method method = new Method(SELECT_COUNT_BY_CONDITION);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        Parameter parameter1 = new Parameter(queryType);
        method.addParameter(parameter1);
        method.addBodyLine("SELECT(\"count(1)\");");
        method.addBodyLine(FROM_AND_WHERE + "(" + parameter1.getName() + ");");

        method.addBodyLine("return toString();");

        return method;
    }

    /**
     * 生成SqlBuilder类的SelectCountByCondition方法
     *
     * @return SelectByCondition method
     */
    protected Method generateSelectByKeyBuilderMethod() {
        Method method = new Method(SELECT_BY_KEY);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        Parameter parameter1 = new Parameter(new FullyQualifiedJavaType(keyType), "key");
        parameter1.addAnnotation("@Param(\"key\")");
        topLevelClass.addImportedType(mybatisParamType);
        method.addParameter(parameter1);
        method.addBodyLine(SELECT_FIELDS + "();");
        method.addBodyLine("FROM(TABLE_NAME+\" t1\");");
        method.addBodyLine("WHERE(\"t1." + delFlagColumn + " = 0\");");
        method.addBodyLine("WHERE(\"t1." + keyColumn + " = #{key}\");");

        method.addBodyLine("return toString()+ \" LIMIT 1\";");

        return method;
    }


    /**
     * 生成SqlBuilder类的delete方法
     *
     * @return delete method
     */
    protected Method generateDeleteBuilderMethod() {
        Method deleteMethod = new Method(DELETE);
        deleteMethod.setReturnType(FullyQualifiedJavaType.getStringInstance());
        deleteMethod.setVisibility(JavaVisibility.PUBLIC);
        Parameter parameter1 = new Parameter(javaKeyType, "key");
        parameter1.addAnnotation("@Param(\"key\")");
        deleteMethod.addParameter(parameter1);
        deleteMethod.addBodyLine("DELETE_FROM(TABLE_NAME);");
        deleteMethod.addBodyLine("WHERE(\"" + keyColumn + " = #{key}\");");
        deleteMethod.addBodyLine("return toString();");
        return deleteMethod;
    }


    /**
     * 生成SqlBuilder类的logicalDelete方法
     *
     * @return logicalDelete method
     */
    protected Method generateLogicalDeleteBuilderMethod() {
        Method method = new Method(LOGICAL_DELETE);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        Parameter parameter1 = new Parameter(javaKeyType, "key");
        parameter1.addAnnotation("@Param(\"key\")");
        method.addParameter(parameter1);
        method.addBodyLine("UPDATE(TABLE_NAME);");
        method.addBodyLine("SET(\"" + delFlagColumn + " = 1\");");
        method.addBodyLine("WHERE(\"" + keyColumn + " = #{key}\");");
        method.addBodyLine("return toString();");
        return method;
    }
}
