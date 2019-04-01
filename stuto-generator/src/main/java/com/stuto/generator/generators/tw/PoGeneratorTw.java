package com.stuto.generator.generators.tw;

import com.stuto.generator.api.IntrospectedColumn;
import com.stuto.generator.api.IntrospectedTable;
import com.stuto.generator.api.dom.java.FullyQualifiedJavaType;
import com.stuto.generator.generators.PoGenerator;

/**
 * @author yongqiang.zhang
 * @version 1.0 , 2018/11/16 11:13
 */
public class PoGeneratorTw extends PoGenerator {

    public PoGeneratorTw(IntrospectedTable table) {
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
