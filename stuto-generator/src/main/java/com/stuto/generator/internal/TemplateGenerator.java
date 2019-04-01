package com.stuto.generator.internal;

import com.alibaba.fastjson.JSONObject;
import com.stuto.core.pub.FileUtil;
import com.stuto.core.pub.StutoException;
import com.stuto.core.pub.StutoRuntimeException;
import com.stuto.core.pub.TimeUtil;
import com.stuto.generator.api.IntrospectedColumn;
import com.stuto.generator.api.IntrospectedTable;
import com.stuto.generator.api.dom.java.FullyQualifiedJavaType;
import com.stuto.generator.config.GeneratorContext;
import com.stuto.generator.util.GeneratorUtil;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.stuto.generator.message.Messages.getString;

/**
 * @author 作者 : zyq
 * 创建时间：2019/3/29 14:37
 * @version 0.0.1
 */
@Slf4j
public class TemplateGenerator implements Generator {

    /**
     * 模板文件根路径
     */
    private static final Resource TEMPLATE_RESOURCE = new ClassPathResource("templates");

    /**
     * 模板生成器名称
     */
    protected String generatorName;

    protected static final String GENERATOR_NAME_PO = "PoGenerator";
    protected static final String GENERATOR_NAME_QUERY = "QueryGenerator";
    protected static final String GENERATOR_NAME_VIEW = "ViewGenerator";
    protected static final String GENERATOR_NAME_MAPPER = "MapperGenerator";
    protected static final String GENERATOR_NAME_SERVICE = "ServiceGenerator";

    /**
     * 文件名称
     */
    protected String fileName;

    /**
     * 文件类型
     */
    protected String fileType;

    /**
     * 数据库表对象
     */
    protected IntrospectedTable introspectedTable;

    /**
     * 读取到的该生成器的配置信息
     */
    protected JSONObject generatorCfg;

    /**
     * java类型
     */
    protected FullyQualifiedJavaType javaType;

    // 相关po类
    protected FullyQualifiedJavaType poType;
    // 相关query类
    protected FullyQualifiedJavaType queryType;
    // 相关view 类
    protected FullyQualifiedJavaType viewType;
    // 相关mapper 类
    protected FullyQualifiedJavaType mapperType;
    // 相关service 类
    protected FullyQualifiedJavaType serviceType;

    protected String keyColumn;
    protected String keyType;
    protected FullyQualifiedJavaType javaKeyType;


    /**
     * freemarker 模板
     */
    private Template template = null;

    /**
     * 数据存储对象
     */
    protected Map<String, Object> dataMap;

    protected String targetProject;
    protected String targetPackage;

    /**
     * 文件名处理器
     */
    protected ClassNameHandler classNameHandler;

//    public TemplateGenerator() {
//        super();
//    }

    public TemplateGenerator(IntrospectedTable introspectedTable, String generatorName) {
        this.introspectedTable = introspectedTable;
        this.generatorName = generatorName;
    }

    public void setTable(IntrospectedTable introspectedTable) {
        this.introspectedTable = introspectedTable;
    }

    protected void wrapData(){
        // 设置数据
        dataMap = new HashMap<>();
        dataMap.put("date", TimeUtil.dateToYmd(LocalDate.now()));
        dataMap.put("table", introspectedTable);
        dataMap.put("tableName", classNameHandler.wrapClassName(introspectedTable).toLowerCase());
        dataMap.put("config", cfgJson);
        dataMap.put("generatorCfg", generatorCfg);

        dataMap.put("package", javaType.getPackageName());
        dataMap.put("fileName", javaType.getShortName());

        dataMap.put("fileName", javaType.getShortName());
        dataMap.put("javaKeyType", javaKeyType);

        dataMap.put("poType", poType);
        dataMap.put("queryType", queryType);
        dataMap.put("viewType", viewType);
        dataMap.put("mapperType", mapperType);
        dataMap.put("serviceType", serviceType);
    }

    void wrapRelateClassName(){
        // po类的全名
        poType = new FullyQualifiedJavaType(classNameHandler.wrapFullClassName(introspectedTable, GENERATOR_NAME_PO));
        // query类的全名
        queryType = new FullyQualifiedJavaType(classNameHandler.wrapFullClassName(introspectedTable, GENERATOR_NAME_QUERY));
        // view类的全名
        viewType = new FullyQualifiedJavaType(classNameHandler.wrapFullClassName(introspectedTable, GENERATOR_NAME_VIEW));
        // mapper类的全名
        mapperType = new FullyQualifiedJavaType(classNameHandler.wrapFullClassName(introspectedTable, GENERATOR_NAME_MAPPER));
        // service类的全名
        serviceType = new FullyQualifiedJavaType(classNameHandler.wrapFullClassName(introspectedTable, GENERATOR_NAME_SERVICE));
    }



    protected void init() throws StutoRuntimeException {
        generatorCfg = cfgJson.getJSONObject("generators").getJSONObject("templateGenerators").getJSONObject(generatorName);

        // 处理类名
        this.classNameHandler = GeneratorUtil.getClassNameHandler();
        String className = wrapFullClassName();
        javaType = new FullyQualifiedJavaType(className);
        // 处理其它相关类的类名
        wrapRelateClassName();
        fileName = javaType.getShortName();
//        topLevelClass = new TopLevelClass(className);
        targetProject = cfgJson.getJSONObject("individual").getString("targetProject");
        targetPackage = javaType.getPackageName();
        fileType = generatorCfg.getString("fileType");

        IntrospectedColumn primaryKeyColumn = introspectedTable.getFirstPrimaryKeyColumn();
        if (primaryKeyColumn != null) {
            keyColumn = primaryKeyColumn.getColumnName();
            keyType = GeneratorContext.fieldTypeMap.get(primaryKeyColumn.getTypeName().toLowerCase());
            javaKeyType = new FullyQualifiedJavaType(keyType);
        }


        // 处理FreeMarker
        Configuration configuration = new Configuration(new Version("2.3.28"));
        TemplateLoader templateLoader = null;
        try {
            templateLoader = new FileTemplateLoader(TEMPLATE_RESOURCE.getFile(), true);
        } catch (IOException e) {
            e.printStackTrace();
            throw new StutoRuntimeException("模板根路径为找到【"+TEMPLATE_RESOURCE+"】");
        }
        configuration.setTemplateLoader(templateLoader);

        // step4 加载模版文件

        try {
            template = configuration.getTemplate(generatorCfg.getString("templatePath"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new StutoRuntimeException("未找到模板文件:"+"【" + generatorCfg.getString("templatePath") + "】！");
        }
    }

    /**
     * 封装类名,返回的类名应当包含完整的包名路径
     *
     * @return
     */
    public String wrapFullClassName() {
        return classNameHandler.wrapFullClassName(introspectedTable,generatorName);
    }

    @Override
    public String getFormattedContent() {
        init();
        wrapData();
        StringWriter stringWriter = new StringWriter();
        try {
            template.process(dataMap, stringWriter);
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
            throw new StutoRuntimeException("Freemarker 编译模板失败");
        }
        return stringWriter.toString();
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
        targetFile = new File(directory, fileName + fileType);
        source = getFormattedContent();
        if (targetFile.exists()) {
            //文件已存在
            if (GeneratorContext.overwriteEnabled) {
                //覆盖
                log.warn(getString("Warning.11", //$NON-NLS-1$
                    targetFile.getAbsolutePath()));
            } else {
                //添加新文件
                targetFile = FileUtil.getUniqueFile(directory, fileName);
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
