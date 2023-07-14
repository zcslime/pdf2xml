package com.quandashi.qz.utils;

import com.spire.pdf.PdfDocument;
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
 * @Date 2023/2/3 18:30
 * @Version 1.0
 * @Describe 按行读取pdf中的文字
 */

public class GetLinesFromPDF extends PDFTextStripper{
    static List<String> lines = new ArrayList<String>();

    public GetLinesFromPDF() throws IOException {
    }

    /**
     * @throws IOException If there is an error parsing the document.
     */
    public static void main( String[] args ) throws IOException {
        PdfDocument pdf = new PdfDocument();
        pdf.loadFromFile( "D:\\user\\pdf\\类似商品分类表(OCR).pdf");
// 保存为Word格式
        pdf.saveToFile( "D:\\user\\pdf\\类似商品分类表(OCR).DOCX");
        PDDocument document = null;
        String fileName = "D:\\user\\pdf\\类似商品分类表(OCR).pdf";
        try {
            document = PDDocument.load( new File(fileName) );
            PDFTextStripper stripper = new GetLinesFromPDF();
            stripper.setSortByPosition( true );
            stripper.setStartPage( 0 );
            stripper.setEndPage( document.getNumberOfPages() );

            Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            stripper.writeText(document, dummy);

            // print lines
            for(String line:lines){
                System.out.println(line);
            }
        }
        finally {
            if( document != null ) {
                document.close();
            }
        }
    }

    /**
     * Override the default functionality of PDFTextStripper.writeString()
     */
    @Override
    protected void writeString(String str, List<TextPosition> textPositions) throws IOException {
        lines.add(str);
        // you may process the line here itself, as and when it is obtained
    }
}
