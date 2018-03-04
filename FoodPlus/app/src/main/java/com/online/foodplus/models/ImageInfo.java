package com.online.foodplus.models;

/**
 * Created by 1918 on 25-Nov-16.
 */

public class ImageInfo {

    protected int image;
    protected String description;

    public ImageInfo() {
    }

    public ImageInfo(int image, String description) {
        this.image = image;
        this.description = description;
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
