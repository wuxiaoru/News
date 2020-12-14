package com.bjfu.news.controller;


import lombok.extern.slf4j.Slf4j;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;

//转换文档为pdf
@Slf4j
public class OpenOffice2PdfUtils {

    /**
     * @param args
     */
    private static OfficeManager officeManager;
    private static String WIN_OFFICE_HOME = "C:\\Program Files (x86)\\OpenOffice 4\\";//C:\Program Files (x86)
    private static String LIN_OFFICE_HOME = "/opt/openoffice4";
    private static int port[] = {8100};

    public static void convert2PDF(String inputFile, String outputFile) throws FileNotFoundException {//File file, String pdfUrl
        try {
            startService();
            System.out.println("进行文档转换转换:" + inputFile + " --> " + outputFile);
            OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
            converter.convert(new File(inputFile), new File(outputFile));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            stopService();
        }
    }

    // 打开服务器
    public static void startService() {
        DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
        try {
            System.out.println("准备启动服务....");
            String OFFICE_HOME = LIN_OFFICE_HOME;
            String os = System.getProperty("os.name");
            if (!StringUtils.isEmpty(os) && os.toLowerCase().startsWith("win")) {
                System.out.println("当前是 windows 系统");
                OFFICE_HOME = WIN_OFFICE_HOME;
            }
            configuration.setOfficeHome(OFFICE_HOME);// 设置OpenOffice.org安装目录
            configuration.setPortNumbers(port); // 设置转换端口，默认为8100
            configuration.setTaskExecutionTimeout(1000 * 60 * 5L);// 设置任务执行超时为5分钟
            configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);// 设置任务队列超时为24小时

            officeManager = configuration.buildOfficeManager();
            officeManager.start(); // 启动服务
            System.out.println("office转换服务启动成功!");
        } catch (Exception ce) {
            System.out.println("office转换服务启动失败!详细信息:" + ce);
        }
    }

    // 关闭服务器
    public static void stopService() {
        System.out.println("关闭office转换服务....");
        if (officeManager != null) {
            officeManager.stop();
        }
        System.out.println("关闭office转换成功!");
    }
}