package com.online.foodplus.models;

import java.util.ArrayList;

/**
 * Created by thanhthang on 12/12/2016.
 */

public class Display {
    private String id, title, description, icon, type, total, cid, tid;
    private ArrayList<Base> data;

    private ArrayList<String> images;
    private Base object;
    private boolean round;

    public Display() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotal() {
        return total;
    }

    public Base getObject() {
        return object;
    }

    public void setObject(Base object) {
        this.object = object;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Base> getData() {
        return data;
    }

    public void setData(ArrayList<Base> data) {
        this.data = data;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isRound() {
        return round;
    }

    public void setRound(boolean round) {
        this.round = round;
    }

}
