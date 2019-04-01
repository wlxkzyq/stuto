package com.stuto.core.pub;

import java.io.IOException;
import java.net.URL;

/**
 * @author yongqiang.zhang
 * @version 1.0 , 2018/11/14 17:44
 */
public class PathUtil {

    /**
     * 获取当前线程所在的class的classpath根目录URL（需处理第一个字符\）
     * 例子：D:/workingSpace2/tools/target/classes/
     * @return  文件执行路径
     */
    public static String getClassPath() throws IOException {
        String classPath="";
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        String os = System.getProperty("os.name");
        if( os.toLowerCase().startsWith("win") ) {
            classPath = url.getPath().substring(1, url.getPath().length());
        } else {
            classPath = url.getPath();
        }
        return classPath;
    }




}
