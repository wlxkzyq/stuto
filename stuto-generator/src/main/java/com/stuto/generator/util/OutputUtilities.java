package com.stuto.generator.util;

import com.stuto.generator.api.dom.java.FullyQualifiedJavaType;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author yongqiang.zhang
 * @version 1.0 , 2018/11/12 16:34
 */
public class OutputUtilities {

    //缩进级别 4 个空格
    private static final String INDENT_LEVEL4 = "    ";
    //缩进级别 4 个空格
    private static final String INDENT_LEVEL2 = "  ";
    //换行符
    private static final String lineSeparator;

    static {
        String ls = System.getProperty("line.separator"); //$NON-NLS-1$
        if (ls == null) {
            ls = "\n"; //$NON-NLS-1$
        }
        lineSeparator = ls;
    }

    private OutputUtilities() {
        super();
    }

    /**
     * java代码缩进,一个缩进级别4个空格
     * @param sb    a StringBuilder to append to
     * @param indentLevel   the required indent level
     */
    public static void javaIndent(StringBuilder sb, int indentLevel) {
        for (int i = 0; i < indentLevel; i++) {
            sb.append(INDENT_LEVEL4); //$NON-NLS-1$
        }
    }


    /**
     * xml代码缩进,一个缩进级别2个空格
     * @param sb    a StringBuilder to append to
     * @param indentLevel   the required indent level
     */
    public static void xmlIndent(StringBuilder sb, int indentLevel) {
        for (int i = 0; i < indentLevel; i++) {
            sb.append(INDENT_LEVEL2); //$NON-NLS-1$
        }
    }

    /**
     * 插入换行符
     * @param sb    the StringBuilder to be appended to
     */
    public static void newLine(StringBuilder sb) {
        sb.append(lineSeparator);
    }

    /**
     * returns a unique set of "import xxx;" Strings for the set of types
     *
     * @param importedTypes
     * @return
     */
    public static Set<String> calculateImports(
        Set<FullyQualifiedJavaType> importedTypes) {
        StringBuilder sb = new StringBuilder();
        Set<String> importStrings = new TreeSet<String>();
        for (FullyQualifiedJavaType fqjt : importedTypes) {
            for (String importString : fqjt.getImportList()) {
                sb.setLength(0);
                sb.append("import "); //$NON-NLS-1$
                sb.append(importString);
                sb.append(';');
                importStrings.add(sb.toString());
            }
        }

        return importStrings;
    }


}
