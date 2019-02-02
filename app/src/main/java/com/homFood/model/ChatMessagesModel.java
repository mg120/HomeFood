package com.homFood.model;

/**
 * Created by Ma7MouD on 8/14/2018.
 */

public class ChatMessagesModel {

    private boolean success ;
    private String sender_id ;
    private String sender_name ;
    private String recive_id ;
    private String message ;
    private String recive_name ;
    private String date ;

    public ChatMessagesModel(boolean success, String sender_id, String sender_name, String recive_id, String message, String recive_name, String date) {
        this.success = success;
        this.sender_id = sender_id;
        this.sender_name = sender_name;
        this.recive_id = recive_id;
        this.message = message;
        this.recive_name = recive_name;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRecive_name() {
        return recive_name;
    }

    public void setRecive_name(String recive_name) {
        this.recive_name = recive_name;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getRecive_id() {
        return recive_id;
    }

    public void setRecive_id(String recive_id) {
        this.recive_id = recive_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
