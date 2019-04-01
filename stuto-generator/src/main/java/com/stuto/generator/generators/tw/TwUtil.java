package com.stuto.generator.generators.tw;

import com.stuto.generator.api.IntrospectedColumn;
import com.stuto.generator.api.dom.java.FullyQualifiedJavaType;
import com.stuto.generator.api.dom.java.PrimitiveTypeWrapper;
import com.stuto.generator.config.GeneratorContext;

/**
 * @author 作者 : zyq
 * 创建时间：2019/3/25 11:45
 * @version 0.0.1
 */
public class TwUtil {

    /**
     * TW系统表名 以T开头(T_tIMESHEET),生java bean时需要去掉"T_"
     * @param tableName
     * @return
     */
    public static String wrapClassName(String tableName){
        return tableName.substring(tableName.indexOf("_")+1);
    }

    /**
     * 重写生产PO数据库字段类型对照,因为TW要求数据库int类型的ID 列都应该是LONG 类型的
     * @param column
     * @return
     */
    public static FullyQualifiedJavaType wrapJavaType(IntrospectedColumn column){
        String columnName = column.getColumnName().toLowerCase();
        if(columnName.indexOf("id")>-1 && column.getTypeName().toLowerCase().equals("int")){
            return PrimitiveTypeWrapper.getLongInstance();
        }else{
            return new FullyQualifiedJavaType(
                GeneratorContext.fieldTypeMap.get(column.getTypeName().toLowerCase()));
        }
    }
}
