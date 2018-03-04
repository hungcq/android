package com.online.foodplus.models;

/**
 * Created by thanhthang on 06/12/2016.
 */

public class Media {
    private String background;
    private int type;

    public Media() {
    }

    public Media(String background) {
        this.background = background;
    }

    public Media(String background, int type) {
        this.background = background;
        this.type = type;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
