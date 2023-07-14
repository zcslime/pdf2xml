package com.quandashi.qz.bean;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * @Author zc
 * @PackageName pdf2xml
 * @Package com.quandashi.qz.bean
 * @Date 2023/3/17 17:01
 * @Version 1.0
 * @Describe
 */

public class PdfemleBean implements Serializable,Comparable {
    private static final long serialVersionUID = 1604353096977201016L;
    // todo 文字或图片标记
    private String chenck;
    // todo 文字字符串
    private String fontchar;
    // todo  x坐标
    private float point_x;
    // todo  y坐标
    private float point_y;
    // todo  元素的高
    private float height;
    // todo  元素的宽
    private float width;
    // todo   图片x像素
    private float scale_x;
    // todo  图片y像素
    private float scale_y;
    // todo  图片对象
    private BufferedImage images;

    public String getChenck() {
        return chenck;
    }

    public void setChenck(String chenck) {
        this.chenck = chenck;
    }

    public String getFontchar() {
        return fontchar;
    }

    public void setFontchar(String fontchar) {
        this.fontchar = fontchar;
    }

    public float getPoint_x() {
        return point_x;
    }

    public void setPoint_x(float point_x) {
        this.point_x = point_x;
    }

    public float getPoint_y() {
        return point_y;
    }

    public void setPoint_y(float point_y) {
        this.point_y = point_y;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getScale_x() {
        return scale_x;
    }

    public void setScale_x(float scale_x) {
        this.scale_x = scale_x;
    }

    public float getScale_y() {
        return scale_y;
    }

    public void setScale_y(float scale_y) {
        this.scale_y = scale_y;
    }

    public BufferedImage getImages() {
        return images;
    }

    public void setImages(BufferedImage images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "PdfemleBean{" +
                "chenck='" + chenck + '\'' +
                ", fontchar='" + fontchar + '\'' +
                ", point_x=" + point_x +
                ", point_y=" + point_y +
                ", height=" + height +
                ", width=" + width +
                ", scale_x=" + scale_x +
                ", scale_y=" + scale_y +
                ", images=" + images +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        PdfemleBean o2 = (PdfemleBean) o;
        if(this.getPoint_y()>o2.getPoint_y()){
            return -1;
        }else if(this.getPoint_y()==o2.getPoint_y()){
             return 0;
        }else {
            return 1;
        }
//        if(this.getPoint_y()-o2.getPoint_y()<=o2.getHeight()||this.getPoint_y()==o2.getPoint_y()){
//            if(this.getPoint_x()<o2.getPoint_x()){
//                return -1;
//            }else if(this.getPoint_x()==o2.getPoint_x()){
//                return 0;
//            } else {
//                return 1;
//            }
//        }else if(this.getPoint_y()-o2.getPoint_y()>o2.getHeight()){
//            return 1;
//        }else {
//            return -1;
//        }
//        if(this.getPoint_y()>o2.getPoint_y()){
//            return 0;
//        }else if(this.getPoint_y()==o2.getPoint_y()&&this.getPoint_y()-o2.getPoint_y()<=o2.getHeight()){
//            if(this.getPoint_x()<o2.getPoint_x()){
//                return -1;
//            }else if(this.getPoint_x()==o2.getPoint_x()){
//                return 0;
//            } else {
//                return 1;
//            }
//        }else {
//            return 1;
//        }
    }
}
