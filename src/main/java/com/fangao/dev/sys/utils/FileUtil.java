package com.fangao.dev.sys.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
    /**
     * 文件上传方法
     * @param file 文件byte[]
     * @param filePath 文件上传路径
     * @param fileName 文件保存路径
     * @throws IOException
     * @throws Exception
     * void
     */
    public static void uploadFile(byte[] file, String filePath, String fileName) throws IOException {

        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }


}
