package com.stuto.core.pub;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * @author yongqiang.zhang
 * @version 1.0 , 2018/11/27 8:09
 */
public class FileUtilTest {


    @Test
    public void writeFile() throws Exception {
        File file = new File("C:\\Users\\57095\\Desktop\\123.txt");
        System.out.println(file.exists());
        FileUtil.writeFile(file,"213","UTF-8");
    }


    @Test
    public void getUniqueFile() throws Exception {
        File file = new File("C:\\Users\\57095\\Desktop");
        System.out.println(FileUtil.getUniqueFile(file, "123.txt"));
    }


    @Test
    public void getDirectory() throws Exception {
        File file = new File("C:\\Users\\57095\\Desktop");
        System.out.println(FileUtil.getPackageDirectory("D:ttt", "123.txt"));
    }

}
