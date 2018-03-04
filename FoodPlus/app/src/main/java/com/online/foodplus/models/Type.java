package com.online.foodplus.models;

/**
 * Created by thanhthang on 24/12/2016.
 */

public class Type {
    private String title;
    private int limit,count;

    public Type() {
    }

    public Type(String title, int limit, int count) {
        this.title = title;
        this.limit = limit;
        this.count = count;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
