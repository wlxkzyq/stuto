package com.stuto.generator;

import com.stuto.generator.api.IntrospectedTable;
import com.stuto.generator.config.GeneratorContext;
import com.stuto.generator.generators.*;
import com.stuto.generator.generators.tw.*;
import com.stuto.generator.internal.TemplateGenerator;

import java.util.List;

/**
 * @author 作者 : zyq
 * 创建时间：2019/3/29 11:02
 * @version 0.0.1
 */
public class Start {

    public static void main(String[] args) {
        generateTw();



    }



    public static void generate(){

        // 获取根据配置文件读取到的数据库表对象(配置文件cfg.json)
        List<IntrospectedTable> introspectedTables = GeneratorContext.getIntrospectedTables();
        // 获取第一个表对象,如果要生成多个表,改循环即可
        IntrospectedTable table = introspectedTables.get(0);
        // 设置表所属的业务模块,如果不设置,默认使用配置文件配置的模块(用于将代码生成到不同的业务目录下)
        table.getTableConfig().setModule("system");

        //创建一个实体类生成器(生成器跟table绑定)
        PoGenerator poGenerator = new PoGenerator(table);
        // 打印改生成器针对table要生成的代码
        System.out.println(poGenerator.getFormattedContent());
        // 将代码输出到相应的文件夹
        poGenerator.writeToFile();

        // 以下的都是创建各种生成器,不重复介绍,仅注释名字
        // mapper生成器
        MapperGenerator mapperGenerator = new MapperGenerator();
        mapperGenerator.setTable(table);
        System.out.println(mapperGenerator.getFormattedContent());
        mapperGenerator.writeToFile();

        // view生成器
        ViewGenerator viewGenerator = new ViewGenerator(table);
        System.out.println(viewGenerator.getFormattedContent());
        viewGenerator.writeToFile();

        // query生成器
        QueryGenerator queryGenerator = new QueryGenerator(table);
        System.out.println(queryGenerator.getFormattedContent());
        queryGenerator.writeToFile();

        // mapper 的junit测试类生成器
        MapperTestGenerator mapperTestGenerator = new MapperTestGenerator(table);
        System.out.println(mapperTestGenerator.getFormattedContent());
        mapperTestGenerator.writeToFile();
    }



    public static void generateTw(){
        List<IntrospectedTable> introspectedTables = GeneratorContext.getIntrospectedTables();
        IntrospectedTable table = introspectedTables.get(0);
        table.getTableConfig().setModule("system");

        TemplateGenerator serviceGenerator = new TemplateGeneratorTw(table,"ServiceGenerator");
        System.out.println(serviceGenerator.getFormattedContent());
        serviceGenerator.writeToFile();

        TemplateGenerator serviceImplGenerator = new TemplateGeneratorTw(table,"ServiceImplGenerator");
        System.out.println(serviceImplGenerator.getFormattedContent());
        serviceImplGenerator.writeToFile();

        TemplateGenerator apiGenerator = new TemplateGeneratorTw(table,"ApiGenerator");
        System.out.println(apiGenerator.getFormattedContent());
        apiGenerator.writeToFile();

        PoGenerator poGenerator = new PoGeneratorTw(table);
        System.out.println(poGenerator.getFormattedContent());
        poGenerator.writeToFile();

        MapperGenerator mapperGenerator = new MapperGeneratorTw();
        mapperGenerator.setTable(table);
        System.out.println(mapperGenerator.getFormattedContent());
        mapperGenerator.writeToFile();

        ViewGenerator viewGenerator = new ViewGeneratorTw(table);
        System.out.println(viewGenerator.getFormattedContent());
        viewGenerator.writeToFile();

        QueryGenerator queryGenerator = new QueryGeneratorTw(table);
        System.out.println(queryGenerator.getFormattedContent());
        queryGenerator.writeToFile();

        MapperTestGenerator mapperTestGenerator = new MapperTestGeneratorTw(table);
        System.out.println(mapperTestGenerator.getFormattedContent());
        mapperTestGenerator.writeToFile();
    }




}
