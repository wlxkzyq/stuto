package com.stuto.generator.internal;

import com.alibaba.fastjson.JSONObject;
import com.stuto.core.pub.*;
import com.stuto.generator.api.dom.java.*;
import com.stuto.generator.config.GeneratorContext;
import com.stuto.generator.generators.util.ConfigUtil;
import com.stuto.generator.util.GeneratorUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static com.stuto.generator.message.Messages.getString;

/**
 * @author yongqiang.zhang
 * @version 1.0 , 2018/11/16 18:27
 */
@Slf4j
public abstract class DefaultTopLevelClassGenerator extends TopLevelClassGenerator {

    private static String JAVA_FILE_SUFFIX = ".java";
    protected String targetProject;
    protected String targetPackage;
    protected String generatorName;
    protected JSONObject generatorCfg;

    public DefaultTopLevelClassGenerator() {

    }

//    abstract public JSONObject getGeneratorCfg();

    @Override
    protected void init() {
        generatorCfg = ConfigUtil.getGeneratorCfg(generatorName);
        this.classNameHandler = GeneratorUtil.getClassNameHandler();
        String className = wrapFullClassName();
        topLevelClass = new TopLevelClass(className);
        targetProject = cfgJson.getJSONObject("individual").getString("targetProject");
        targetPackage = topLevelClass.getType().getPackageName();

    }


    /**
     * 封装类名,返回的类名应当包含完整的包名路径
     *
     * @return
     */
    @Override
    public String wrapFullClassName() {
        return classNameHandler.wrapFullClassName(introspectedTable,generatorName);
    }

    /**
     * 添加类注释
     */
    @Override
    protected void addClassDoc() {
        // 添加类注释
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * " + introspectedTable.getRemarks() + "【" + introspectedTable.getTableName() + "】");
        topLevelClass.addJavaDocLine(" * @author " + cfgJson.getJSONObject("individual").getString("name"));
        topLevelClass.addJavaDocLine(" * @email " + cfgJson.getJSONObject("individual").getString("email"));
        topLevelClass.addJavaDocLine(" * @date " + TimeUtil.dateToYmd(LocalDate.now()));
        topLevelClass.addJavaDocLine(" */");
    }

    /**
     * 添加类注解
     */
    @Override
    protected void addClassAnnotation() {

    }

    @Override
    protected void buildClass() {
        // 添加类注释
        addClassDoc();
        // 添加注解
        addClassAnnotation();
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);

        List<Field> fields = buildFields();
        if (fields != null && fields.size() > 0) {
            fields.forEach(topLevelClass::addField);
        }

        List<Method> methods = buildMethods();
        if (methods != null && methods.size() > 0) {
            methods.forEach(topLevelClass::addMethod);
        }

        // 设置继承
        String extendsString = generatorCfg.getString("extends");
        if (!StringUtil.isBlank(extendsString)) {
            FullyQualifiedJavaType extendsType = new FullyQualifiedJavaType(extendsString);
            topLevelClass.setSuperClass(extendsType);
            topLevelClass.addImportedType(extendsType);
        }

    }

    @Override
    protected List<Field> buildFields() {
        return null;
    }

    @Override
    protected List<Method> buildMethods() {
        return null;
    }

    @Override
    public String getFormattedContent() {
        init();
        buildClass();
        return topLevelClass.getFormattedContent();
    }

    @Override
    public void writeToFile() {
        File targetFile;
        String source;
        String resourcePath = generatorCfg.getString("resourcePath");
        targetProject = targetProject+"/"+resourcePath;
        File directory = null;
        try {
            directory = FileUtil.getPackageDirectory(targetProject, targetPackage);
        } catch (StutoException e) {
            e.printStackTrace();
        }
        targetFile = new File(directory, topLevelClass.getType().getShortName() + JAVA_FILE_SUFFIX);
        source = getFormattedContent();
        if (targetFile.exists()) {
            //文件已存在
            if (GeneratorContext.overwriteEnabled) {
                //覆盖
                log.warn(getString("Warning.11", //$NON-NLS-1$
                    targetFile.getAbsolutePath()));
            } else {
                //添加新文件
                targetFile = FileUtil.getUniqueFile(directory, topLevelClass.getType().getShortName());
                log.warn(getString(
                    "Warning.2", targetFile.getAbsolutePath())); //$NON-NLS-1$
            }
        } else {
            //文件不存在
        }
        FileUtil.writeFile(targetFile, source, GeneratorContext.fileEncoding);
        log.info("【" + targetFile + "】生成完毕！");
    }
}
