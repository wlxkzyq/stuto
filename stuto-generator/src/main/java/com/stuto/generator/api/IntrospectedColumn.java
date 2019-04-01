package com.stuto.generator.api;

import lombok.Data;

/**
 * @author yongqiang.zhang
 * @version 1.0 , 2018/11/15 18:00
 */
@Data
public class IntrospectedColumn {

    private String tableCatalog;
    private String tableSchema;
    private String tableName;
    private String columnName;
    private int dataType;
    private String typeName;
    private String defaultValue;
    /**
     * 总长度，包括小数点
     */
    private int columnSize;
    /**
     * 是否可空
     * 0 : 不可空
     * 1 : 可空
     */
    private int nullable;
    /**
     * 小数长度
     */
    private int decimalDigits;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 是不是增长类型
     * YES : 是
     * NO  : 不是
     */
    private String isAutoIncrement;

    /**
     * 是否主键
     */
    private boolean primaryKey;

    /**
     * 外键字段
     */
    private String foreignKey;
}
