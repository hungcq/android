package com.online.foodplus.models;

/**
 * Created by thanhthang on 14/01/2017.
 */

public class Comment {
    private String username, content, avatar, comment_id,comment_photo;

    public Comment() {
    }

    public String getComment_photo() {
        return comment_photo;
    }

    public void setComment_photo(String comment_photo) {
        this.comment_photo = comment_photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }
}

