package com.bjfu.news.untils;

import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    static String WIN_FILE_PATH = "D://file/";
    static String LIN_FILE_PATH = "/usr/local/news/doc/";

    public static List<String> uploadFile(HttpServletRequest request) {
        InputStream in = null;
        List<String> list = new ArrayList<>();
        String FILE_REAL_PATH = LIN_FILE_PATH;
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            FILE_REAL_PATH = WIN_FILE_PATH;
        }
        try {
            // 为了获取文件，这个类是必须的
            MultiValueMap<String, MultipartFile> map = ((MultipartHttpServletRequest) request).getMultiFileMap();
            // 获取到文件的列表
            List<MultipartFile> listFile = map.get("file");
            for (MultipartFile multipartFile : listFile) {
                in = multipartFile.getInputStream();
                String originalFilename = multipartFile.getOriginalFilename();
                //保存到本地服务器
                String filePath = FileUtils.saveFile(in, originalFilename, FILE_REAL_PATH);
                list.add(filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //一定要关闭资源
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static String saveFile(InputStream inputStream, String fileName, String realPath) {
        OutputStream os = null;
        try {
            // 1K的数据缓冲
            byte[] bs = new byte[1024];

            // 读取到的数据长度
            int len;
            // 输出的文件流保存到本地文件
            File tempFile = new File(realPath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            os = new FileOutputStream(tempFile.getPath() + File.separator + fileName);
            // 开始读取
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 完毕，关闭所有链接
            try {
                os.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    public static void downloadLocal(HttpServletResponse response, String path) throws FileNotFoundException, UnsupportedEncodingException {
        // 下载本地文件
        String FILE_REAL_PATH = LIN_FILE_PATH;
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            FILE_REAL_PATH = WIN_FILE_PATH;
        }
        // 读到流中
        InputStream inStream = new FileInputStream(FILE_REAL_PATH + path);// 文件的存放路径
        // 设置输出的格式
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
        response.addHeader("content-Type", "application/octet-stream");
        //URLEncoder.encode(fileName, "UTF-8")
        path = new String(path.getBytes("UTF-8"), "iso-8859-1");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + path + "\"");
        // 循环取出流中的数据
        byte[] b = new byte[100];
        int len;
        try {
            while ((len = inStream.read(b)) > 0)
                response.getOutputStream().write(b, 0, len);
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
