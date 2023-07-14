package com.quandashi.qz.main;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.istack.internal.logging.Logger;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @Author zc
 * @PackageName pdf2xml
 * @Package com.quandashi.qz.main
 * @Date 2023/2/6 11:19
 * @Version 1.0
 * @Describe
 */

public class ImageMeagePdf {
    private static Logger logger = Logger.getLogger(ImageMeagePdf.class);
    public static void main(String[] args) throws Exception {
        String imagesPath="D:\\user\\test\\";
        String outPdfPath = "D:\\user\\test\\test.pdf";
        File[] imageFiles= new File[]{};
        File file = new File(imagesPath);
        File[] files = file.listFiles();
//        for (int i = 0; i < files.length; i++) {
//            System.out.println(files[i]);
//        }
//        String[] imgNameArray = new String[] { "1_0.png", "1_1.png", "1_2.png", "1_3.png", "1_4.png", "1_5.png",
//                "1_6.png", "1_7.png", "1_8.png", "1_9.png", "1_10.png", "1_11.png", };
         imagesToPdf("D:\\user\\test\\", "D:\\user\\test\\test1.pdf", files);


//        long time1 = System.currentTimeMillis();
//        toPdf("D:\\user\\test\\", "D:\\user\\test.pdf");
//        long time2 = System.currentTimeMillis();
//        int time = (int) ((time2 - time1) / 1000);
//        System.out.println("执行了：" + time + "秒！");
    }
    /**
     *
     * @param outPdfPath
     * @param outPdfFilepath 生成pdf文件路径
     * @param imageFiles 需要转换的图片File类Array,按array的顺序合成图片
     */
    public static void imagesToPdf(String outPdfPath, String outPdfFilepath, File[] imageFiles) throws Exception {

        logger.info("进入图片合成PDF工具方法");

        File file = new File(outPdfFilepath);
        // 第一步：创建一个document对象。
        Document document = new Document();
        document.setMargins(0, 0, 0, 0);
        // 第二步：
        // 创建一个PdfWriter实例，
        PdfWriter.getInstance(document, new FileOutputStream(file));
        // 第三步：打开文档。
        document.open();
        // 第四步：在文档中增加图片。
        int len = imageFiles.length;

        for (int i = 0; i < len; i++) {
            if (imageFiles[i].getName().toLowerCase().endsWith(".bmp")
                    || imageFiles[i].getName().toLowerCase().endsWith(".jpg")
                    || imageFiles[i].getName().toLowerCase().endsWith(".jpeg")
                    || imageFiles[i].getName().toLowerCase().endsWith(".gif")
                    || imageFiles[i].getName().toLowerCase().endsWith(".png")
                    || imageFiles[i].getName().toLowerCase().endsWith(".tif")) {
                String temp = imageFiles[i].getAbsolutePath();
                logger.info("图片路径："+temp);
                Image img = Image.getInstance(temp);
                img.setAlignment(Image.ALIGN_CENTER);
                img.scaleAbsolute(597, 844);// 直接设定显示尺寸
                // 根据图片大小设置页面，一定要先设置页面，再newPage（），否则无效
                //document.setPageSize(new Rectangle(img.getWidth(), img.getHeight()));
                document.setPageSize(new Rectangle(597, 844));
                document.newPage();
                document.add(img);
            }
        }
        // 第五步：关闭文档。
        document.close();
        logger.info("图片合成PDF完成");
    }
}
