package com.online.foodplus.models;

import java.util.ArrayList;

/**
 * Created by thanhthang on 17/01/2017.
 */

public class Content {
    private String title, icon, tid,cid;
    private ArrayList<Display> data;

    public Content() {
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
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

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public ArrayList<Display> getData() {
        return data;
    }

    public void setData(ArrayList<Display> data) {
        this.data = data;
    }
}
