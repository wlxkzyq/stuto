package com.stuto.generator.generators;

import com.stuto.core.pub.TimeUtil;
import com.stuto.generator.api.IntrospectedColumn;
import com.stuto.generator.api.IntrospectedTable;
import com.stuto.generator.api.dom.java.*;
import com.stuto.generator.config.GeneratorContext;
import com.stuto.generator.internal.DefaultTopLevelClassGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * mapper 测试类生成器
 * @author 作者 : zyq
 * 创建时间：2019/3/27 18:53
 * @version 0.0.1
 */
public class MapperTestGenerator extends DefaultTopLevelClassGenerator {

    protected static final String GENERATOR_NAME = "MapperTestGenerator";
    protected static final String GENERATOR_NAME_PO = "PoGenerator";
    protected static final String GENERATOR_NAME_QUERY = "QueryGenerator";
    protected static final String GENERATOR_NAME_VIEW = "ViewGenerator";
    protected static final String GENERATOR_NAME_MAPPER = "MapperGenerator";

    // 测试insert方法的方法名
    protected final String INSERT_NAME = "insert";

    // 测试updateByKey方法的方法名
    protected final String UPDATE_BY_KEY_NAME = "updateByKey";

    // 测试selectByCondition方法的方法名
    protected final String SELECT_BY_CONDITION_NAME = "selectByCondition";

    // 测试selectCountByCondition方法的方法名
    protected final String SELECT_COUNT_BY_CONDITION_NAME = "selectCountByCondition";

    // 测试selectByKey方法的方法名
    protected final String SELECT_BY_KEY_NAME = "selectByKey";

    // 测试delete方法的方法名
    protected final String DELETE_NAME = "delete";

    // 测试logicalDelete方法的方法名
    protected final String LOGICAL_DELETE_NAME = "logicalDelete";


    protected FullyQualifiedJavaType poType;
    protected FullyQualifiedJavaType queryType;
    protected FullyQualifiedJavaType viewType;
    protected FullyQualifiedJavaType mapperType;
    protected String keyColumn;
    protected String keyType;
    protected FullyQualifiedJavaType javaKeyType;


    public MapperTestGenerator(IntrospectedTable table) {
        this.introspectedTable = table;
    }


    @Override
    protected void init() {
        super.generatorName=GENERATOR_NAME;
        super.init();
        poType = new FullyQualifiedJavaType(wrapPoClassName());
        queryType = new FullyQualifiedJavaType(wrapQueryClassName());
        viewType = new FullyQualifiedJavaType(wrapViewClassName());
        mapperType = new FullyQualifiedJavaType(wrapMapperClassName());
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

    /**
     * mapper类的全名
     *
     * @return
     */
    String wrapMapperClassName() {
        return classNameHandler.wrapFullClassName(introspectedTable, GENERATOR_NAME_MAPPER);
    }


    protected void addClassDoc() {
        // 添加类注释
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * " + introspectedTable.getRemarks() + " MapperTest");
        topLevelClass.addJavaDocLine(" * @author " + cfgJson.getJSONObject("individual").getString("name"));
        topLevelClass.addJavaDocLine(" * @email " + cfgJson.getJSONObject("individual").getString("email"));
        topLevelClass.addJavaDocLine(" * @date " + TimeUtil.dateToYmd(LocalDate.now()));
        topLevelClass.addJavaDocLine(" */");
    }

    @Override
    protected List<Field> buildFields() {
        List<Field> fields = new ArrayList<>();
        fields.add(buildMapperField());

        return fields;
    }

    protected Field buildMapperField(){
        Field field = new Field("mapper",mapperType);
        topLevelClass.addImportedType(mapperType);
        field.setVisibility(JavaVisibility.PRIVATE);
        field.addAnnotation("@Autowired");
        topLevelClass.addImportedType(new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"));

        return field;
    }

    @Override
    protected List<Method> buildMethods() {
        List<Method> methods = new ArrayList<>();
        methods.add(buildInsertMethod());
        methods.add(buildUpdateByKeyMethod());
        methods.add(buildSelectByConditionMethod());
        methods.add(buildSelectCountByConditionMethod());
        methods.add(buildSelectByKeyMethod());
        methods.add(buildDeleteMethod());
        methods.add(buildLogicalDeleteMethod());



        return methods;
    }

    /**
     * 返回一个基本的junit 的测试类方法
     * @return
     */
    protected Method getTestMethod() {
        FullyQualifiedJavaType exception = new FullyQualifiedJavaType("java.lang.Exception");
        Method method = new Method();
        method.addAnnotation("@Test");
        method.setReturnType(null);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addException(exception);
        topLevelClass.addImportedType(new FullyQualifiedJavaType("org.junit.Test"));
        return method;
    }

    /**
     * 为测试方法添加主键行
     * @param method
     * @return
     */
    protected void addKeyBody(Method method) {
        if("java.lang.Integer".equals(keyType)){
            method.addBodyLine("Integer key = 1;");
        }
        if("java.lang.Long".equals(keyType)){
            method.addBodyLine("Long key = 1L;");
        }
        if("java.time.LocalDate".equals(keyType)){
            method.addBodyLine("LocalDate key = LocalDate.now();");
            topLevelClass.addImportedType(new FullyQualifiedJavaType("java.time.LocalDate"));
        }
        topLevelClass.addImportedType(javaKeyType);
    }

    /**
     * 生成mapperTest类的insert接口方法
     *
     * @return
     */
    protected Method buildInsertMethod() {
        Method method = getTestMethod();
        method.setName(INSERT_NAME);
        method.addBodyLine(poType.getShortName()+" entity = new "+poType.getShortName()+"();");
        method.addBodyLine("int count = mapper."+INSERT_NAME+"(entity);");
        method.addBodyLine("System.out.println(count);");
        topLevelClass.addImportedType(poType);
        return method;
    }


    /**
     * 生成mapperTest类的updateByKey接口方法
     *
     * @return
     */
    protected Method buildUpdateByKeyMethod() {
        Method method = getTestMethod();
        method.setName(UPDATE_BY_KEY_NAME);
        method.addBodyLine(poType.getShortName()+" entity = new "+poType.getShortName()+"();");
        method.addBodyLine("int count = mapper."+UPDATE_BY_KEY_NAME+"(entity);");
        method.addBodyLine("System.out.println(count);");
        topLevelClass.addImportedType(poType);
        return method;
    }

    /**
     * 生成mapperTest类的selectByCondition接口方法
     *
     * @return
     */
    protected Method buildSelectByConditionMethod() {
        Method method = getTestMethod();
        method.setName(SELECT_BY_CONDITION_NAME);
        method.addBodyLine(queryType.getShortName()+" query = new "+queryType.getShortName()+"();");
        method.addBodyLine("List<"+viewType.getShortName()+"> list = mapper."+SELECT_BY_CONDITION_NAME+"(query);");
        method.addBodyLine("System.out.println(list);");
        topLevelClass.addImportedType(queryType);
        topLevelClass.addImportedType(viewType);
        topLevelClass.addImportedType(new FullyQualifiedJavaType("java.util.List"));
        return method;
    }

    /**
     * 生成mapperTest类的selectCountByCondition接口方法
     *
     * @return
     */
    protected Method buildSelectCountByConditionMethod() {
        Method method = getTestMethod();
        method.setName(SELECT_COUNT_BY_CONDITION_NAME);
        method.addBodyLine(queryType.getShortName()+" query = new "+queryType.getShortName()+"();");
        method.addBodyLine("int count = mapper."+SELECT_COUNT_BY_CONDITION_NAME+"(query);");
        method.addBodyLine("System.out.println(count);");
        topLevelClass.addImportedType(queryType);
        return method;
    }


    /**
     * 生成mapperTest类的SelectByKey接口方法
     *
     * @return
     */
    protected Method buildSelectByKeyMethod() {
        Method method = getTestMethod();
        method.setName(SELECT_BY_KEY_NAME);
        addKeyBody(method);
        method.addBodyLine(viewType.getShortName()+" view = mapper."+SELECT_BY_KEY_NAME+"(key);");
        method.addBodyLine("System.out.println(view);");
        topLevelClass.addImportedType(viewType);
        return method;
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
        method.addBodyLine("int count = mapper."+DELETE_NAME+"(key);");
        method.addBodyLine("System.out.println(count);");

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
        method.addBodyLine("int count = mapper."+LOGICAL_DELETE_NAME+"(key);");
        method.addBodyLine("System.out.println(count);");

        return method;
    }


}
