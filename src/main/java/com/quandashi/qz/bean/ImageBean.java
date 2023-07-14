package com.quandashi.qz.bean;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * @Author zc
 * @PackageName pdf2xml
 * @Package com.quandashi.qz.bean
 * @Date 2023/3/16 15:24
 * @Version 1.0
 * @Describe
 */

public class ImageBean implements Serializable {
    private static final long serialVersionUID = 8329645551860664632L;
    private String imageName;
    private float point_x;
    private float point_y;
    private float scale_x;
    private float scale_y;
    private float height;
    private float width;
    BufferedImage images;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
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

    public BufferedImage getImages() {
        return images;
    }

    public void setImages(BufferedImage images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "ImageBean{" +
                "imageName='" + imageName + '\'' +
                ", point_x=" + point_x +
                ", point_y=" + point_y +
                ", scale_x=" + scale_x +
                ", scale_y=" + scale_y +
                ", height=" + height +
                ", width=" + width +
                ", images=" + images +
                '}';
    }
}
