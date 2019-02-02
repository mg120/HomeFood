package com.homFood.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ma7MouD on 5/27/2018.
 */

public class ChatModel implements Parcelable {

    private String sender_id;
    private String sender_name;
    private String message;
    private String meddage_time;


    public ChatModel(String sender_id, String sender_name, String message, String meddage_time) {
        this.sender_id = sender_id;
        this.sender_name = sender_name;
        this.message = message;
        this.meddage_time = meddage_time;
    }

    private ChatModel(Parcel in) {
        sender_id = in.readString();
        sender_name = in.readString();
        message = in.readString();
        meddage_time = in.readString();
    }

    public static final Creator<ChatModel> CREATOR = new Creator<ChatModel>() {
        @Override
        public ChatModel createFromParcel(Parcel in) {
            return new ChatModel(in);
        }

        @Override
        public ChatModel[] newArray(int size) {
            return new ChatModel[size];
        }
    };

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMeddage_time() {
        return meddage_time;
    }

    public void setMeddage_time(String meddage_time) {
        this.meddage_time = meddage_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(sender_id);
        parcel.writeString(sender_name);
        parcel.writeString(message);
        parcel.writeString(meddage_time);
    }
}
