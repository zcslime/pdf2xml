package com.quandashi.qz.utils;

import com.quandashi.qz.bean.CharTextBean;
import com.quandashi.qz.bean.PdfemleBean;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zc
 * @PackageName pdf2xml
 * @Package com.quandashi.qz.utils
 * @Date 2023/2/3 17:41
 * @Version 1.0
 * @Describe 读取pdf中文字和其坐标
 */

public class GetCharLocationAndSize extends PDFTextStripper {
    public static void main( String[] args ) throws IOException {
        PDDocument document = null;
        String fileName = "D:\\user\\pdf\\CN115779587A.pdf";
        try {
            document = PDDocument.load(new File(fileName));
            int pageSize  = document.getNumberOfPages();
//            int pageSize = 3;
            GetCharLocationAndSize fontext = new GetCharLocationAndSize();
            for (int i = 1; i < pageSize+1; i++) {
                System.out.println(i);
//                System.out.println("i=" + i + ", i+1=" + (i+1));
//                // TODO 文字读取
                fontext.setSortByPosition( true );
                fontext.setStartPage(i);
                fontext.setEndPage(i);
//                if(i==pageSize+1){
//                    fontext.setStartPage(i+1);
//                    fontext.setEndPage(i+1);
//                }else{
//                    fontext.setStartPage(i);
//                    fontext.setEndPage(i);
//                }
//                fontext.setStartPage(i);
//                fontext.setEndPage(i);
                Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
                fontext.writeText(document, dummy);
                List<PdfemleBean> charList = GetCharLocationAndSize.charList;
                System.out.println("-----------------------");
                System.out.println(charList.get(0));
                System.out.println(charList.get(charList.size()-1));
                System.out.println("-----------------------");
//                for (int j = 0; j < charList.size(); j++) {
////                    System.out.println(charList.get(j));
//                }
//                charList.clear();
            }


        }
        finally {
            if( document != null ) {
                document.close();
            }
        }
    }

    public static List<PdfemleBean> charList = new ArrayList<>();
    /**
     * @throws IOException If there is an error parsing the document.
     */
    public GetCharLocationAndSize() throws IOException {
    }
    /**
     * Override the default functionality of PDFTextStripper.writeString()
     */
    @Override
    public void writeString(String string, List<TextPosition> textPositions) throws IOException {
        for (TextPosition text : textPositions) {
//            CharTextBean charTextBean = new CharTextBean();
//            charTextBean.setFontchar(text.getUnicode());
//            charTextBean.setPoint_x(text.getEndX());
//            charTextBean.setPoint_y(text.getEndY());
//            charTextBean.setHeight(text.getHeightDir());
//            charTextBean.setWidth(text.getWidthDirAdj());
////            System.out.println(text.getUnicode()+ " [(X=" + text.getXDirAdj() + ",Y=" +
////                    text.getYDirAdj() + ":x："+text.getEndX() + "y："+text.getEndY()+ ":x1："+text.getX() + "y1："+text.getY()
////                    + ") height=" + text.getHeightDir() + " width=" +
////                    text.getWidthDirAdj() + "]");
//            charList.add(charTextBean);
           if(text.getEndY()>60){
               PdfemleBean pdfe = new PdfemleBean();
               pdfe.setChenck("文字");
               pdfe.setFontchar(text.getUnicode());
               pdfe.setPoint_x(text.getEndX());
               pdfe.setPoint_y(text.getEndY());
//            pdfe.setPoint_x(text.getXDirAdj());
//            pdfe.setPoint_y(text.getYDirAdj());
               pdfe.setHeight(text.getHeightDir());
               pdfe.setWidth(text.getWidthDirAdj());
               charList.add(pdfe);
           }
        }
    }

}
