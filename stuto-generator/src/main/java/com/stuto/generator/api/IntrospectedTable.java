package com.stuto.generator.api;

import com.stuto.generator.config.TableConfig;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author yongqiang.zhang
 * @version 1.0 , 2018/11/15 17:59
 */
@Data
public class IntrospectedTable {

    private String tableCatalog;
    private String tableSchema;
    private String tableName;
    private String tableType;
    private String remarks;

    private List<IntrospectedColumn> columns;
    private Set<IntrospectedPrimaryKey> primaryKeys;
    private Set<IntrospectedForeignKey> foreignKeys;

    /**
     * 第一个主键列
     */
    private IntrospectedColumn firstPrimaryKeyColumn;

    /**
     * 表配置
     */
    private TableConfig tableConfig;

    public void init(String tableCatalog,String tableSchema,String tableName){
        this.tableCatalog=tableCatalog;
        this.tableSchema=tableSchema;
        this.tableName=tableName;
    }

}
