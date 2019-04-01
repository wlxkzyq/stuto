package com.stuto.generator.util;

import com.stuto.core.pub.StringUtil;
import com.stuto.core.pub.StutoError;
import com.stuto.generator.api.dom.java.*;
import com.stuto.generator.config.GeneratorContext;
import com.stuto.generator.internal.ClassNameHandler;

import java.io.IOException;

/**
 * @author yongqiang.zhang
 * @version 1.0 , 2018/11/12 16:14
 * @date
 */
public class GeneratorUtil {

    public static ClassNameHandler getClassNameHandler(){
        // 获取配置的类名处理器
        String classNameHandlerFullName = GeneratorContext.cfgJson.getJSONObject("generators").getString("classNameHandler");
        try {
            Class<?> classNameHandlerClazz = Class.forName(classNameHandlerFullName);
            return (ClassNameHandler)classNameHandlerClazz.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            throw new StutoError("获取类名处理器失败,请检查配置文件的<classNameHandler>");

        }
    }

    /**
     * 生成SqlBuilder类的字段
     * @param fieldName 字段名称
     * @return  field
     */
    public static Field generateBuilderField(String fieldName){
        Field insertField = new Field(fieldName,FullyQualifiedJavaType.getStringInstance());
        insertField.setFinal(true);
        insertField.setStatic(true);
        insertField.setVisibility(JavaVisibility.PRIVATE);
        insertField.setInitializationString("\""+StringUtil.getCamelCaseString(fieldName,false)+"\"");
        return insertField;
    }

    /**
     * 生成SqlBuilder类的insert方法
     * @param columns   需要生成的字段
     * @param parameter insert方法的参数
     * @return  insert method
     */
    public static Method generateInsertBuilderMethod(String[] columns, String parameter) {
        Method insertMethod = new Method("insert");
        insertMethod.setReturnType(FullyQualifiedJavaType.getStringInstance());
        insertMethod.setVisibility(JavaVisibility.PUBLIC);
        Parameter parameter1 = new Parameter(new FullyQualifiedJavaType(parameter), parameter.substring(0, 1).toLowerCase() + parameter.substring(1));
        insertMethod.addParameter(parameter1);
        insertMethod.addBodyLine("INSERT_INTO(TABLE_NAME);");
        for (String column : columns) {
            insertMethod.addBodyLine("INTO_COLUMNS(\"" + column + "\");");
            insertMethod.addBodyLine("INTO_VALUES(\"#{" + StringUtil.getCamelCaseString(column, false) + "}\");");
        }

        insertMethod.addBodyLine("return toString();");

        return insertMethod;
    }

    /**
     * 生成SqlBuilder类的updateById方法
     * @param columns   需要生成的字段
     * @param parameter updateById 方法的参数
     * @return  updateById method
     */
    public static Method generateUpdateBuilderMethod(String[] columns, String parameter) {
        Method updateByIdMethod = new Method("updateById");
        updateByIdMethod.setReturnType(FullyQualifiedJavaType.getStringInstance());
        updateByIdMethod.setVisibility(JavaVisibility.PUBLIC);
        Parameter parameter1 = new Parameter(new FullyQualifiedJavaType(parameter), parameter.substring(0, 1).toLowerCase() + parameter.substring(1));
        updateByIdMethod.addParameter(parameter1);
        updateByIdMethod.addBodyLine("UPDATE(TABLE_NAME);");
        for (String column : columns) {
            updateByIdMethod.addBodyLine("SET(\""+column+" = #{"+StringUtil.getCamelCaseString(column, false)+"}\");");
        }

        updateByIdMethod.addBodyLine("WHERE(\"id = #{id}\");");
        updateByIdMethod.addBodyLine("return toString();");

        return updateByIdMethod;
    }

    public static Method generateGetSelectBuilderMethod(String[] columns) {
        Method getSelect = new Method("getSelect");
//        getSelect.setReturnType();
        getSelect.setVisibility(JavaVisibility.PRIVATE);
        for (String column : columns) {
            getSelect.addBodyLine("SELECT(\"t1."+column+" as "+StringUtil.getCamelCaseString(column, false)+"\");");
        }

        return getSelect;
    }




    public static void main(String[] args) throws IOException {

//        String insertBuilderMethodString = generateInsertBuilderMethod(columns, "User").getFormattedContent(0, false);
//        String insertBuilderFieldString = generateBuilderField("INSERT").getFormattedContent(0);
//        System.out.println(insertBuilderFieldString);
//        System.out.println(insertBuilderMethodString);
//
//        String updateByIdBuilderMethodString = generateUpdateBuilderMethod(columns, "User").getFormattedContent(0, false);
//        String updateByIdBuilderFieldString = generateBuilderField("UPDATE_BY_ID").getFormattedContent(0);
//        System.out.println(updateByIdBuilderFieldString);
//        System.out.println(updateByIdBuilderMethodString);
//
//        String GetSelectBuilderMethodString = generateGetSelectBuilderMethod(columns).getFormattedContent(0, false);
//        System.out.println(GetSelectBuilderMethodString);

//        String poString = PoGenerator.generatePoClass(columns,"Tw").getFormattedContent();
//        System.out.println(poString);

//        List<IntrospectedTable> introspectedTables = GeneratorContext.getIntrospectedTables();
//        IntrospectedTable table = introspectedTables.get(0);
//        table.getTableConfig().setModule("system");
//
//        PoGenerator poGenerator = new PoGeneratorTw(table);
//        System.out.println(poGenerator.getFormattedContent());
//        poGenerator.writeToFile();
//
//        MapperGenerator mapperGenerator = new MapperGeneratorTw();
//        mapperGenerator.setTable(table);
//        System.out.println(mapperGenerator.getFormattedContent());
//        mapperGenerator.writeToFile();
//
//        ViewGenerator viewGenerator = new ViewGeneratorTw(table);
//        System.out.println(viewGenerator.getFormattedContent());
//        viewGenerator.writeToFile();
//
//        QueryGenerator queryGenerator = new QueryGeneratorTw(table);
//        System.out.println(queryGenerator.getFormattedContent());
//        queryGenerator.writeToFile();
//
//        MapperTestGenerator mapperTestGenerator = new MapperTestGenerator(table);
//        System.out.println(mapperTestGenerator.getFormattedContent());
//        mapperTestGenerator.writeToFile();


    }

//    String columnString = "ID\n" +
//        "TENANT_ID\n" +
//        "PROJ_NAME\n" +
//        "PROJ_STATUS\n" +
//        "CONTRACT_ID\n" +
//        "PROJ_TEMP_ID\n" +
//        "CUST_IDST\n" +
//        "CUST_REGION\n" +
//        "PLAN_START_DATE\n" +
//        "PLAN_END_DATE\n" +
//        "PM_ID\n" +
//        "PM_EQVA_RATIO\n" +
//        "TOTAL_DAYS\n" +
//        "TOTAL_EQVA\n" +
//        "TOTAL_REIMBURSEMENT\n" +
//        "TOTAL_COST\n" +
//        "EQVA_PRICE\n" +
//        "EPIBOLY_PERMIT\n" +
//        "SUBCONTRACT_PERMIT\n" +
//        "TIMESHEET_PERIOD\n" +
//        "FINISH_APPROVE\n" +
//        "DEPOSIT\n" +
//        "RELATED_PROJ_ID\n" +
//        "PERFORMANCE_DESC\n" +
//        "CLOSE_REASON\n" +
//        "REMARK\n" +
//        "DEL_FLAG\n" +
//        "CREATE_USER_ID\n" +
//        "CREATE_TIME\n" +
//        "MODIFY_USER_ID\n" +
//        "MODIFY_TIME";
//    String[] columns = columnString.split("\n");
}
