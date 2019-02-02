package com.homFood.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ma7MouD on 5/10/2018.
 */

public class CheckoutModel implements Parcelable {

    private String seller_id ;
    private String checkout_pro_id ;
    private String checkout_pro_img ;
    private String checkout_pro_name ;
    private String checkout_pro_category ;
    private String checkout_pro_price ;
    private String checkout_pro_quantity ;

    public CheckoutModel() {
    }

//    public CheckoutModel(String checkout_pro_id, String checkout_pro_img, String checkout_pro_name, String checkout_pro_category, String checkout_pro_price, String checkout_pro_quantity) {
//        this.checkout_pro_id = checkout_pro_id;
//        this.checkout_pro_img = checkout_pro_img;
//        this.checkout_pro_name = checkout_pro_name;
//        this.checkout_pro_category = checkout_pro_category;
//        this.checkout_pro_price = checkout_pro_price;
//        this.checkout_pro_quantity = checkout_pro_quantity;
//    }

    public CheckoutModel(String seller_id, String checkout_pro_id, String checkout_pro_img, String checkout_pro_name, String checkout_pro_category, String checkout_pro_price, String checkout_pro_quantity) {
        this.seller_id = seller_id;
        this.checkout_pro_id = checkout_pro_id;
        this.checkout_pro_img = checkout_pro_img;
        this.checkout_pro_name = checkout_pro_name;
        this.checkout_pro_category = checkout_pro_category;
        this.checkout_pro_price = checkout_pro_price;
        this.checkout_pro_quantity = checkout_pro_quantity;
    }

    protected CheckoutModel(Parcel in) {
        seller_id = in.readString();
        checkout_pro_id = in.readString();
        checkout_pro_img = in.readString();
        checkout_pro_name = in.readString();
        checkout_pro_category = in.readString();
        checkout_pro_price = in.readString();
        checkout_pro_quantity = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(seller_id);
        dest.writeString(checkout_pro_id);
        dest.writeString(checkout_pro_img);
        dest.writeString(checkout_pro_name);
        dest.writeString(checkout_pro_category);
        dest.writeString(checkout_pro_price);
        dest.writeString(checkout_pro_quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CheckoutModel> CREATOR = new Creator<CheckoutModel>() {
        @Override
        public CheckoutModel createFromParcel(Parcel in) {
            return new CheckoutModel(in);
        }

        @Override
        public CheckoutModel[] newArray(int size) {
            return new CheckoutModel[size];
        }
    };

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getCheckout_pro_id() {
        return checkout_pro_id;
    }

    public void setCheckout_pro_id(String checkout_pro_id) {
        this.checkout_pro_id = checkout_pro_id;
    }

    public String getCheckout_pro_img() {
        return checkout_pro_img;
    }

    public void setCheckout_pro_img(String checkout_pro_img) {
        this.checkout_pro_img = checkout_pro_img;
    }

    public String getCheckout_pro_name() {
        return checkout_pro_name;
    }

    public void setCheckout_pro_name(String checkout_pro_name) {
        this.checkout_pro_name = checkout_pro_name;
    }

    public String getCheckout_pro_category() {
        return checkout_pro_category;
    }

    public void setCheckout_pro_category(String checkout_pro_category) {
        this.checkout_pro_category = checkout_pro_category;
    }

    public String getCheckout_pro_price() {
        return checkout_pro_price;
    }

    public void setCheckout_pro_price(String checkout_pro_price) {
        this.checkout_pro_price = checkout_pro_price;
    }

    public String getCheckout_pro_quantity() {
        return checkout_pro_quantity;
    }

    public void setCheckout_pro_quantity(String checkout_pro_quantity) {
        this.checkout_pro_quantity = checkout_pro_quantity;
    }

}
