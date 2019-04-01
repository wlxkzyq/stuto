package com.stuto.generator.config;

import com.alibaba.fastjson.JSONObject;
import com.stuto.core.pub.StringUtil;
import com.stuto.generator.api.IntrospectedTable;
import com.stuto.generator.internal.DatabaseIntrospector;
import com.stuto.generator.util.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * @author yongqiang.zhang
 * @version 1.0 , 2018/11/15 17:41
 */
@Slf4j
public class GeneratorContext {
    private static final String CFG_PATH = "cfg.json";

    private static Resource cfgResource = new ClassPathResource(CFG_PATH);

    /**
     * 配置文件json对象
     */
    public static JSONObject cfgJson;

    /**
     * 读取到的数据库表格
     */
    private static List<IntrospectedTable> introspectedTables;

    /**
     * 数据库列类型与实体类类型对应关系
     */
    public static Map<String,String> fieldTypeMap;

    /**
     * 输出文件是否覆盖
     */
    public static boolean overwriteEnabled;

    /**
     * 输出文件编码
     */
    public static String fileEncoding;


    static {
        init();
    }
    /**
     * 获取全局配置文件
     * @return  配置文件
     */
//    public static JSONObject getCfgJson(){
//        if(cfgJson==null){
//            try {
//                cfgJson = JSONObject.parseObject(IOUtils.toString(cfgResource.getInputStream(), Charset.forName("utf8")));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return cfgJson;
//    }


    /**
     * 初始化
     */
    public static void init(){
        loadConfig();
    }

    public static void loadConfig(){
        fieldTypeMap = new HashMap<>();
        try {
            cfgJson = JSONObject.parseObject(IOUtils.toString(cfgResource.getInputStream(), Charset.forName("utf8")));
            String dbType = cfgJson.getJSONObject("db").getString("dbType");
            JSONObject fieldTypeMapCfg = cfgJson.getJSONObject("db").getJSONObject(dbType).getJSONObject("fieldTypeMap");
            fieldTypeMapCfg.forEach( (String key,Object val) -> fieldTypeMap.put(key,val.toString()));
            overwriteEnabled = cfgJson.getJSONObject("individual").getBoolean("overwriteEnabled");
            fileEncoding = cfgJson.getJSONObject("individual").getString("fileEncoding");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<IntrospectedTable> getIntrospectedTables(){
        if(introspectedTables == null){
            TableChooseConfiguration tableChooseConfiguration = new TableChooseConfiguration();
            JSONObject tableCatalogCfg = cfgJson.getJSONObject("db").getJSONObject("tableCatalog");
            tableChooseConfiguration.setCatalog(tableCatalogCfg.getString("catalog"));
            tableChooseConfiguration.setSchema(tableCatalogCfg.getString("schema"));
            String chooseTables = tableCatalogCfg.getString("chooseTables");
            if(!StringUtil.isBlank(chooseTables)){
                tableChooseConfiguration.setChooseTables(new HashSet<>(Arrays.asList(chooseTables.split(","))));
            }
            String ignoredTables = tableCatalogCfg.getString("ignoredTables");
            if(!StringUtil.isBlank(ignoredTables)){
                tableChooseConfiguration.setIgnoredTables(new HashSet<>(Arrays.asList(ignoredTables.split(","))));
            }
            String ignoredColumns = tableCatalogCfg.getString("ignoredColumns");
            if (StringUtil.isBlank(ignoredColumns)) {
                log.debug("未忽略任何列");
            } else {
                tableChooseConfiguration.setIgnoredColumns(new HashSet<>(Arrays.asList(ignoredColumns.split(","))));
            }
            tableChooseConfiguration.setNameLike(tableCatalogCfg.getString("nameLike"));
            Connection connection = null;
            try {
                connection = ConnectionFactory.getInstance().getConnection();
                DatabaseMetaData meta=connection.getMetaData();
                log.info("当前数据库类型:{}",meta.getDatabaseProductName());

                DatabaseIntrospector di=new DatabaseIntrospector();
                di.setDatabaseMetaData(meta);
                List<IntrospectedTable> list=di.introspectTables(tableChooseConfiguration);
                introspectedTables=list;
                return list;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("读取数据库信息失败！");

            } finally{
                try {
                    assert connection != null;
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return introspectedTables;



    }

}
