package com.stuto.generator.generators.tw;

import com.stuto.generator.api.IntrospectedColumn;
import com.stuto.generator.api.IntrospectedTable;
import com.stuto.generator.api.dom.java.FullyQualifiedJavaType;
import com.stuto.generator.generators.QueryGenerator;

/**
 * @author 作者 : zyq
 * 创建时间：2019/3/25 13:59
 * @version 0.0.1
 */
public class QueryGeneratorTw extends QueryGenerator{

    public QueryGeneratorTw(IntrospectedTable table) {
        super(table);
    }

    /**
     * 重写生产PO数据库字段类型对照,因为TW要求数据库int类型的ID 列都应该是LONG 类型的
     * @param column
     * @return
     */
    @Override
    protected FullyQualifiedJavaType wrapFieldType(IntrospectedColumn column) {
        return TwUtil.wrapJavaType(column);
    }
}
