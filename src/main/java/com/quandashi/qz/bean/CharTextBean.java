package com.quandashi.qz.bean;

import java.io.Serializable;

/**
 * @Author zc
 * @PackageName pdf2xml
 * @Package com.quandashi.qz.bean
 * @Date 2023/2/7 17:43
 * @Version 1.0
 * @Describe
 */

public class CharTextBean implements Serializable {

    private static final long serialVersionUID = 1946247989513164812L;

    private String fontchar;
    private float point_x;
    private float point_y;
    private float height;
    private float width;

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

    @Override
    public String toString() {
        return "CharTextBean{" +
                "fontchar='" + fontchar + '\'' +
                ", point_x=" + point_x +
                ", point_y=" + point_y +
                ", height=" + height +
                ", width=" + width +
                '}';
    }
}
