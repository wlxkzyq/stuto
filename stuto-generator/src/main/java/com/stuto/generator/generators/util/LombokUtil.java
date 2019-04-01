package com.stuto.generator.generators.util;

import com.stuto.generator.api.dom.java.TopLevelClass;

/**
 * @author 作者 : zyq
 * 创建时间：2019/3/25 11:48
 * @version 0.0.1
 */
public class LombokUtil {

    /**
     * 添加lombok 实体类相关的注解
     * @param topLevelClass
     * @param isExtend
     */
    public static void addLombokEntityAnnotation(TopLevelClass topLevelClass,boolean isExtend){
        topLevelClass.addAnnotation("@Data");
        topLevelClass.addAnnotation("@ToString");
        topLevelClass.addImportedType("lombok.Data");
        topLevelClass.addImportedType("lombok.ToString");
        if(isExtend){
            topLevelClass.addAnnotation("@EqualsAndHashCode(callSuper = true)");
            topLevelClass.addImportedType("lombok.EqualsAndHashCode;");
        }
    }
}
