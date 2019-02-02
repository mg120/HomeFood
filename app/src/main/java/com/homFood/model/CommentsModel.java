package com.homFood.model;

/**
 * Created by Ma7MouD on 7/11/2018.
 */

public class CommentsModel {

    private String username;
    private String comment_txt;
    private String rate;

    public CommentsModel() {
    }

    public CommentsModel(String username, String comment_txt, String rate) {
        this.username = username;
        this.comment_txt = comment_txt;
        this.rate = rate;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment_txt() {
        return comment_txt;
    }

    public void setComment_txt(String comment_txt) {
        this.comment_txt = comment_txt;
    }
}
