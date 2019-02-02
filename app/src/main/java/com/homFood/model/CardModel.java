package com.homFood.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ma7MouD on 4/15/2018.
 */

public class CardModel implements Parcelable {

    private String seller_id;
    private String order_pro_id;
    private String order_pro_img;
    private String order_pro_name;
    private String order_pro_price;
    private String order_pro_quantity;

    public CardModel(String seller_id, String order_pro_id, String order_pro_img, String order_pro_name, String order_pro_price, String order_pro_quantity) {
        this.seller_id = seller_id;
        this.order_pro_id = order_pro_id;
        this.order_pro_img = order_pro_img;
        this.order_pro_name = order_pro_name;
        this.order_pro_price = order_pro_price;
        this.order_pro_quantity = order_pro_quantity;
    }

    protected CardModel(Parcel in) {
        seller_id = in.readString();
        order_pro_id = in.readString();
        order_pro_img = in.readString();
        order_pro_name = in.readString();
        order_pro_price = in.readString();
        order_pro_quantity = in.readString();
    }

    public static final Creator<CardModel> CREATOR = new Creator<CardModel>() {
        @Override
        public CardModel createFromParcel(Parcel in) {
            return new CardModel(in);
        }

        @Override
        public CardModel[] newArray(int size) {
            return new CardModel[size];
        }
    };

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getOrder_pro_id() {
        return order_pro_id;
    }

    public void setOrder_pro_id(String order_pro_id) {
        this.order_pro_id = order_pro_id;
    }

    public String getOrder_pro_img() {
        return order_pro_img;
    }

    public void setOrder_pro_img(String order_pro_img) {
        this.order_pro_img = order_pro_img;
    }

    public String getOrder_pro_name() {
        return order_pro_name;
    }

    public void setOrder_pro_name(String order_pro_name) {
        this.order_pro_name = order_pro_name;
    }

    public String getOrder_pro_price() {
        return order_pro_price;
    }

    public void setOrder_pro_price(String order_pro_price) {
        this.order_pro_price = order_pro_price;
    }

    public String getOrder_pro_quantity() {
        return order_pro_quantity;
    }

    public void setOrder_pro_quantity(String order_pro_quantity) {
        this.order_pro_quantity = order_pro_quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(seller_id);
        parcel.writeString(order_pro_id);
        parcel.writeString(order_pro_img);
        parcel.writeString(order_pro_name);
        parcel.writeString(order_pro_price);
        parcel.writeString(order_pro_quantity);
    }

//    public String getOrder_pro_time() {
//        return order_pro_time;
//    }
//
//    public void setOrder_pro_time(String order_pro_time) {
//        this.order_pro_time = order_pro_time;
//    }
}
