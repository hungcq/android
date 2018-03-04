package com.online.foodplus.models;

/**
 * Created by thanhthang on 06/12/2016.
 */

public class Slider {
    private String image, title;

    public Slider() {

    }

    public Slider(String image, String title) {

        this.image = image;
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
