package com.quandashi.qz.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Author zc
 * @PackageName pdf2xml
 * @Package com.quandashi.qz.utils
 * @Date 2023/2/6 9:39
 * @Version 1.0
 * @Describe
 */

public class PDFTextStripper2  extends PDFTextStripper {
    public static void main(String[] args) throws IOException {
        String path = "D:\\user\\pdf\\CN107743643B.pdf";
        File file = new File(path);
        InputStream is = null;
        PDDocument document = null;
        PDFTextStripper2 stripper2 = new PDFTextStripper2();
        if(path.endsWith(".pdf")){
            //打开PDF文件
            document = PDDocument.load(file);

            //获取pdf页数 // 一页一页读取
            int pageSize  = document.getNumberOfPages();
            for (int i = 1; i < pageSize; i++) {
                stripper2.setStartPage(i);
                stripper2.setEndPage(i);
                stripper2.getText(document);//读取当前页的全部内容
                //这里可以自己for循环处理，我为了过滤PDF page的头和页脚，直接读body
                //注意，body里面遇到表格，图片，会放到ls的最后面，方便处理
                List<List<TextPosition>> ll = stripper2.getCharactersByArticle();
          List<TextPosition> ls = ll.get(0);//读取PDF page的header
//                List<TextPosition> ls = ll.get(1);//读取pdf page的body内容
//          List<TextPosition> ls = ll.get(2);//读取PDF page的页码部分
                float y = 0;
                int buttom;//每行距离下面一行的距离
                StringBuffer sentence = new StringBuffer();
                for (int j = 0; j < ls.size(); j++) {
                    String unicode = ls.get(j).getUnicode();
                    System.out.println(unicode);
                }
            }
        }
    }
    public PDFTextStripper2() throws IOException {
    }

    @Override
    public List<List<TextPosition>> getCharactersByArticle() {
        return super.getCharactersByArticle();
    }
}
