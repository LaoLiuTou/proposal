package com.fangao.dev.sys.utils;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;

import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ExportExcelUtils {
    public static File createNewFile(Map<String, Object> beans, File file, String path, String name) {
        XLSTransformer transformer = new XLSTransformer();

        //可以写工具类来生成命名规则
        name = name+".xlsx";
        File newFile = new File(path + name);


        try (InputStream in = new BufferedInputStream(new FileInputStream(file));
             OutputStream out = new FileOutputStream(newFile)) {
            Workbook workbook = transformer.transformXLS(in, beans);
            workbook.write(out);
            out.flush();
            return newFile;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return newFile;
    }

    /**
     * 将服务器新生成的excel从浏览器下载
     *
     * @param response
     * @param excelFile
     */
    public static void downloadFile(HttpServletResponse response, File excelFile) {
        /* 设置文件ContentType类型，这样设置，会自动判断下载文件类型 */
        response.setContentType("multipart/form-data");
        /* 设置文件头：最后一个参数是设置下载文件名 */
        response.setHeader("Content-Disposition", "attachment;filename=" + excelFile.getName());
        try (
                InputStream ins = new FileInputStream(excelFile);
                OutputStream os = response.getOutputStream()
        ) {
            byte[] b = new byte[1024];
            int len;
            while ((len = ins.read(b)) > 0) {
                os.write(b, 0, len);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 浏览器下载完成之后删除服务器生成的文件
     * 也可以设置定时任务去删除服务器文件
     *
     * @param excelFile
     */
    public static void deleteFile(File excelFile) {

        excelFile.delete();
    }

    //如果目录不存在创建目录 存在则不创建
    public static void createDir(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 直接对response输出Excel文件
     * @param fileName 文件名（不包含.xls）
     * @param sheetName 表名
     * @param title 标题名
     * @param fields 属性名
     * @param titles 属性显示名
     * @param datas 数据
     * @param response
     * @throws IOException
     */
    public static void downLoadExcel(String fileName,
                                     String sheetName,
                                     String title,
                                     List<String> fields,
                                     List<String> titles,
                                     List<Map<String, Object>> datas,
                                     HttpServletResponse response)
    throws IOException{
        List<ExcelExportEntity> colList = new ArrayList<ExcelExportEntity>();
        for(int i=0;i<fields.size();i++){
            ExcelExportEntity colEntity = new ExcelExportEntity(titles.get(i),fields.get(i));
            colEntity.setNeedMerge(true);
            colList.add(colEntity);
        }
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(title, sheetName), colList, datas);
        setResponse(fileName + ".xls", response);
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        out.close();
    }

    private static void setResponse(String filename, HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel;charset=utf-8"); //xls类型
        response.setHeader("Content-disposition", "attachment;filename=" + encodeToUTF8(filename));
        response.setHeader("Pragma", "No-cache");
    }
    public static String encodeToUTF8(String urlParam) {
        try {
            return urlParam != null ? URLEncoder.encode(urlParam, "utf-8") : "";
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

//----------------------------------------------- 以下为多个文件压缩下载------------------------------------------------
    public static HttpServletResponse downLoadFiles(List<File> files, String zipPath,HttpServletResponse response) throws Exception {

        try {
            // List<File> 作为参数传进来，就是把多个文件的路径放到一个list里面
            // 创建一个临时压缩文件

            // 临时文件可以放在CDEF盘中，但不建议这么做，因为需要先设置磁盘的访问权限，最好是放在服务器上，方法最后有删除临时文件的步骤

            String zipFilename = zipPath;
            File file = new File(zipFilename);
            file.createNewFile();
            if (!file.exists()) {
                file.createNewFile();
            }
            response.reset();
            // response.getWriter()
            // 创建文件输出流
            FileOutputStream fous = new FileOutputStream(file);
            ZipOutputStream zipOut = new ZipOutputStream(fous);
            zipFile(files, zipOut);
            zipOut.close();
            fous.close();
            return downloadZip(file, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 把接受的全部文件打成压缩包
     */
    public static void zipFile(List files, ZipOutputStream outputStream) {
        int size = files.size();
        for (int i = 0; i < size; i++) {
            File file = (File) files.get(i);
            zipFile(file, outputStream);
        }
    }

    /**
     * 根据输入的文件与输出流对文件进行打包
     */
    public static void zipFile(File inputFile, ZipOutputStream ouputStream) {
        try {
            if (inputFile.exists()) {
                if (inputFile.isFile()) {

                    FileInputStream IN = new FileInputStream(inputFile);
                    BufferedInputStream bins = new BufferedInputStream(IN, 512);
                    ZipEntry entry = new ZipEntry(inputFile.getName());

                    ouputStream.putNextEntry(entry);
                    // 向压缩文件中输出数据
                    int nNumber;
                    byte[] buffer = new byte[512];
                    while ((nNumber = bins.read(buffer)) != -1) {
                        ouputStream.write(buffer, 0, nNumber);
                    }
                    // 关闭创建的流对象
                    bins.close();
                    IN.close();
                } else {
                    try {
                        File[] files = inputFile.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            zipFile(files[i], ouputStream);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HttpServletResponse downloadZip(File file, HttpServletResponse response) {
        if (file.exists() == false) {
            System.out.println("待压缩的文件目录：" + file + "不存在.");
        } else {
            try {
                // 以流的形式下载文件。
                InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                // 清空response
                response.reset();

                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                response.addHeader("Content-Length", "" + file.length());
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + new String(file.getName().getBytes("GB2312"), "ISO8859-1"));

                toClient.write(buffer);
                toClient.flush();
                toClient.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    File f = new File(file.getPath());
                    f.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

}
