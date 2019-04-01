package com.stuto.core.pub;

import java.io.*;
import java.util.StringTokenizer;

/**
 * @author yongqiang.zhang
 * @version 1.0 , 2018/11/14 19:25
 */
public class FileUtil {

    /**
     * 将字符串写入到文件
     * <p><b>如果文件未创建,则自动创建</b>
     * @param file	要写入的文件
     * @param content	字符串
     * @param fileEncoding	文件编码
     */
    public static void writeFile(File file, String content, String fileEncoding){
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file, false);
            OutputStreamWriter osw;
            if (fileEncoding == null) {
                osw = new OutputStreamWriter(fos);
            } else {
                osw = new OutputStreamWriter(fos, fileEncoding);
            }
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(content);
            bw.close();
            osw.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("写入文件失败！！");
        }

    }

    /**
     * 获取不重复的文件
     * @param directory 目录
     * @param fileName  文件名
     * @return  名字不重复的文件
     */
    public static File getUniqueFile(File directory, String fileName){
        File answer = null;

        // try up to 1000 times to generate a unique file name
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < 1000; i++) {
            sb.setLength(0);
            sb.append(fileName);
            sb.append('.');
            sb.append(i);

            File testFile = new File(directory, sb.toString());
            if (!testFile.exists()) {
                answer = testFile;
                break;
            }
        }

        if (answer == null) {
            throw new RuntimeException("获取唯一文件名失败,【"+directory.getAbsolutePath()+"】目录下"); //$NON-NLS-1$
        }

        return answer;
    }

    /**
     * 获取包路径(package com.stuto.common 会转化成com/stuto/common)
     * @param targetProject 文件夹根路径,必须存在
     * @param targetPackage 包名
     * @return  最终路径
     * @throws Exception    目录创建失败异常
     */
    public static File getPackageDirectory(String targetProject, String targetPackage) throws StutoException {
        // targetProject is interpreted as a directory that must exist
        //
        // targetPackage is interpreted as a sub directory, but in package
        // format (with dots instead of slashes). The sub directory will be
        // created
        // if it does not already exist

        File project = new File(targetProject);
        if (!project.isDirectory()) {
            throw new StutoException("targetProject目录不存在:"+targetProject);
        }

        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(targetPackage, "."); //$NON-NLS-1$
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
            sb.append(File.separatorChar);
        }

        File directory = new File(project, sb.toString());
        if (!directory.isDirectory()) {
            boolean rc = directory.mkdirs();
            if (!rc) {
                throw new StutoException("目录创建失败:"+directory.getAbsolutePath());
            }
        }

        return directory;
    }

}
