package com.quandashi.qz.utils;

import com.quandashi.qz.bean.ImageBean;
import com.quandashi.qz.bean.PdfemleBean;
import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.apache.pdfbox.contentstream.operator.state.Concatenate;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.contentstream.operator.DrawObject;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.contentstream.PDFStreamEngine;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.state.Concatenate;
import org.apache.pdfbox.contentstream.operator.state.Restore;
import org.apache.pdfbox.contentstream.operator.state.Save;
import org.apache.pdfbox.contentstream.operator.state.SetGraphicsStateParameters;
import org.apache.pdfbox.contentstream.operator.state.SetMatrix;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author zc
 * @PackageName pdf2xml
 * @Package com.quandashi.qz.utils
 * @Date 2023/2/3 16:59
 * @Version 1.0
 * @Describe 读取pdf中图片和其坐标信息
 */

public class GetImageLocationsAndSize extends PDFStreamEngine {
    public static void main( String[] args ) throws IOException
    {
        PDDocument document = null;
        String pn = "CN115779587A";
        String fileName = "D:\\user\\pdf\\CN115779587A.pdf";
        try
        {
            document = PDDocument.load( new File(fileName) );
            GetImageLocationsAndSize printer = new GetImageLocationsAndSize();
            int pageSize  = document.getNumberOfPages();
            System.out.println(pageSize);
            for (int i = 1; i < pageSize+1; i++) {
                PDPage pdPage = document.getPages().get(i-1);
                System.out.println( "\n\nProcessing page: " + i +"\n---------------------------------");
                printer.processPage(pdPage);
                List<PdfemleBean> imglist = GetImageLocationsAndSize.imglist;
                List<PdfemleBean> newimglist = new ArrayList<PdfemleBean>();
                for (int im = 0; im < imglist.size(); im++) {
                    PdfemleBean imageBean = imglist.get(im);
                    imageBean.setFontchar(pn+ "_" + i + "_" + im + ".png");
                    FileOutputStream out = new FileOutputStream("D:\\ideaplace\\pdf2xml\\image\\" + pn+ "_" + i + "_" + im + ".png");
                    ImageIO.write(imageBean.getImages(), "png", out);
                    newimglist.add(imageBean);
                }
                imglist.clear();
            }
        }
        finally
        {
            if( document != null )
            {
                document.close();
            }
        }
    }



    public static List<PdfemleBean> imglist = new ArrayList<>();
    /**
     * @throws IOException If there is an error loading text stripper properties.
     */
    public GetImageLocationsAndSize() throws IOException
    {
        // preparing PDFStreamEngine
        addOperator(new Concatenate());
        addOperator(new DrawObject());
        addOperator(new SetGraphicsStateParameters());
        addOperator(new Save());
        addOperator(new Restore());
        addOperator(new SetMatrix());
    }
    /**
     * @throws IOException If there is an error parsing the document.
     */


    /**
     * @param operator The operation to perform.
     * @param operands The list of arguments.
     *
     * @throws IOException If there is an error processing the operation.
     */
    @Override
    protected void processOperator( Operator operator, List<COSBase> operands) throws IOException
    {
        String operation = operator.getName();
        if( "Do".equals(operation) )
        {
            COSName objectName = (COSName) operands.get( 0 );
            // get the PDF object
            PDXObject xobject = getResources().getXObject( objectName );
            // check if the object is an image object
            if(xobject instanceof PDImageXObject)
            {
                PDImageXObject image = (PDImageXObject)xobject;
                BufferedImage images = image.getImage();
                float imageWidth = image.getWidth();
                float imageHeight = image.getHeight();
//                System.out.println("\nImage [" + objectName.getName() + "]");
                Matrix ctmNew = getGraphicsState().getCurrentTransformationMatrix();
                float imageXScale = ctmNew.getScalingFactorX();
                float imageYScale = ctmNew.getScalingFactorY();
                float translateX = ctmNew.getTranslateX();
                float translateY = ctmNew.getTranslateY();
                if((imageWidth!=160.0&&imageHeight!=18.0)&&(imageWidth!=116.0&&imageHeight!=17.0)
                        &(imageWidth!=113.0&&imageHeight!=89.0)&(imageWidth!=74.0&&imageHeight!=74.0)&(imageWidth!=135.0&&imageHeight!=45.0)){
//                    ImageBean im = new ImageBean();
//                    im.setImageName(UUID.randomUUID() + ".png");
//                    im.setPoint_x(translateX);
//                    im.setPoint_y(translateY);
//                    im.setScale_x(imageXScale);
//                    im.setScale_y(imageYScale);
//                    im.setHeight(imageHeight);
//                    im.setWidth(imageWidth);
//                    im.setImages(images);
//                    imglist.add(im);
                    PdfemleBean bean = new PdfemleBean();
                    bean.setChenck("图片");
                    bean.setPoint_x(translateX);
                    bean.setPoint_y(translateY);
                    bean.setScale_x(imageXScale);
                    bean.setScale_y(imageYScale);
                    bean.setHeight(imageHeight);
                    bean.setWidth(imageWidth);
                    bean.setImages(images);
                    imglist.add(bean);
//                    System.out.println(im);
//                    FileOutputStream out = new FileOutputStream("D:\\ideaplace\\pdf2xml\\image\\" + UUID.randomUUID() + ".png");
//                    ImageIO.write(images, "png", out);
//                    // position of image in the pdf in terms of user space units
//                    System.out.println("position in PDF = " + ctmNew.getTranslateX() + ", " + ctmNew.getTranslateY() + " in user space units");
//                    // raw size in pixels
//                    System.out.println("raw image size  = " + imageWidth + ", " + imageHeight + " in pixels");
//                    // displayed size in user space units
//                    System.out.println("displayed size  = " + imageXScale + ", " + imageYScale + " in user space units");
                }

            }
            else if(xobject instanceof PDFormXObject)
            {
                PDFormXObject form = (PDFormXObject)xobject;
                showForm(form);
            }
        }
        else
        {
            super.processOperator( operator, operands);
        }
    }

}
