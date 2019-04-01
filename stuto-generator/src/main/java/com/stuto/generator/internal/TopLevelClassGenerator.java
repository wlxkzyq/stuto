package com.stuto.generator.internal;

import com.stuto.generator.api.IntrospectedTable;
import com.stuto.generator.api.dom.java.Field;
import com.stuto.generator.api.dom.java.Method;
import com.stuto.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * @author yongqiang.zhang
 * @version 1.0 , 2018/11/16 17:55
 */
public abstract class TopLevelClassGenerator implements Generator {

    /**
     * 封装的顶层class
     */
    protected TopLevelClass topLevelClass;

    /**
     * 数据库表对象
     */
    protected IntrospectedTable introspectedTable;

    public TopLevelClassGenerator() {
        super();
    }

    public void setTable(IntrospectedTable introspectedTable) {
        this.introspectedTable = introspectedTable;
    }

    abstract void init();

    /**
     * 生产的类名处理器
     */
    protected ClassNameHandler classNameHandler;

//    /**
//     * 处理类的包名
//     *
//     * @return 包名:rootPackage+module名+generator的package
//     */
//    public abstract String wrapClassPackage();

//    /**
//     * 封装类名,不包含路径的简单类名
//     *
//     * @return
//     */
//    public abstract String wrapClassName();

    /**
     * 封装类名
     * @return
     */
    public abstract String wrapFullClassName();

    /**
     * 添加类注释
     */
    abstract void addClassDoc();

    /**
     * 添加类注解
     */
    abstract void addClassAnnotation();

    abstract void buildClass();

    abstract List<Field> buildFields();

    abstract List<Method> buildMethods();

}
