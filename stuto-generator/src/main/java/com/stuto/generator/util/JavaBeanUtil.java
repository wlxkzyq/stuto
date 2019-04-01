package com.stuto.generator.util;

import java.util.Locale;

/**
 * java类工具类
 * @author yongqiang.zhang
 * @version 1.0 , 2018/5/18 10:41
 */
public class JavaBeanUtil {

    /**
     * Utility class. No instances allowed
     */
    private JavaBeanUtil() {
        super();
    }

    /**
     * eMail > geteMail() firstName > getFirstName() URL > getURL() XAxis >
     * getXAxis() a > getA() B > invalid - this method assumes that this is not
     * the case. Call getValidPropertyName first. Yaxis > invalid - this method
     * assumes that this is not the case. Call getValidPropertyName first.
     * @param property  属性
     * @param isBooleanPrimitive    是否是boolean属性(强调:是否是基本数据类型的boolean,如果是Boolean,应该传false)
     * @return  属性的get方法名
     */
    public static String getGetterMethodName(String property,boolean isBooleanPrimitive) {
        StringBuilder sb = new StringBuilder();

        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }

        if (isBooleanPrimitive) {
            sb.insert(0, "is"); //$NON-NLS-1$
        } else {
            sb.insert(0, "get"); //$NON-NLS-1$
        }

        return sb.toString();
    }

    /**
     * eMail > geteMail() firstName > getFirstName() URL > getURL() XAxis >
     * getXAxis() a > getA() B > invalid - this method assumes that this is not
     * the case. Call getValidPropertyName first. Yaxis > invalid - this method
     * assumes that this is not the case. Call getValidPropertyName first.
     * <p>
     *     该方法不考虑基本数据类型boolean 的get方法使用is的情况,生成get开头的get方法
     * </p>
     * @param property  属性
     * @return  属性的get方法名
     */
    public static String getGetterMethodName(String property) {
        StringBuilder sb = new StringBuilder();
        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }
        sb.insert(0, "get"); //$NON-NLS-1$
        return sb.toString();
    }

    /**
     * JavaBeans rules:
     *
     * eMail > seteMail() firstName > setFirstName() URL > setURL() XAxis >
     * setXAxis() a > setA() B > invalid - this method assumes that this is not
     * the case. Call getValidPropertyName first. Yaxis > invalid - this method
     * assumes that this is not the case. Call getValidPropertyName first.
     *
     * @param property  属性
     * @return the setter method name
     */
    public static String getSetterMethodName(String property) {
        StringBuilder sb = new StringBuilder();

        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }

        sb.insert(0, "set"); //$NON-NLS-1$

        return sb.toString();
    }


    /**
     * This method ensures that the specified input string is a valid Java
     * property name. The rules are as follows:
     *
     * 1. If the first character is lower case, then OK 2. If the first two
     * characters are upper case, then OK 3. If the first character is upper
     * case, and the second character is lower case, then the first character
     * should be made lower case
     *
     * eMail > eMail firstName > firstName URL > URL XAxis > XAxis a > a B > b
     * Yaxis > yaxis
     *
     * @param inputString   input string
     * @return the valid property name
     */
    public static String getValidPropertyName(String inputString) {
        String answer;

        if (inputString == null) {
            answer = null;
        } else if (inputString.length() < 2) {
            answer = inputString.toLowerCase(Locale.US);
        } else {
            if (Character.isUpperCase(inputString.charAt(0))
                    && !Character.isUpperCase(inputString.charAt(1))) {
                answer = inputString.substring(0, 1).toLowerCase(Locale.US)
                        + inputString.substring(1);
            } else {
                answer = inputString;
            }
        }

        return answer;
    }


}
