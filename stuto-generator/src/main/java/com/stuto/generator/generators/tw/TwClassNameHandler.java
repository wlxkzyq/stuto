package com.stuto.generator.generators.tw;

import com.stuto.core.pub.StringUtil;
import com.stuto.generator.api.IntrospectedTable;
import com.stuto.generator.internal.DefaultClassNameHandler;

/**
 * @author 作者 : zyq
 * 创建时间：2019/3/25 15:58
 * @version 0.0.1
 */
public class TwClassNameHandler extends DefaultClassNameHandler {

    /**
     * 封装类名,不包含路径的简单类名
     *
     * @return
     */
    public String wrapClassName(IntrospectedTable introspectedTable) {
        return TwUtil.wrapClassName(introspectedTable.getTableName());
    }

}
