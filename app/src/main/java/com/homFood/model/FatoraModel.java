package com.homFood.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ma7MouD on 5/13/2018.
 */

public class FatoraModel implements Parcelable {

    private String product_name ;
    private String product_img;
    private String food_type ;
    private String quantity ;
    private String price ;

    public FatoraModel() {
    }

    public FatoraModel(String product_name, String product_img, String food_type, String quantity, String price) {
        this.product_name = product_name;
        this.product_img = product_img;
        this.food_type = food_type;
        this.quantity = quantity;
        this.price = price;
    }

    protected FatoraModel(Parcel in) {
        product_name = in.readString();
        product_img = in.readString();
        food_type = in.readString();
        quantity = in.readString();
        price = in.readString();
    }

    public static final Creator<FatoraModel> CREATOR = new Creator<FatoraModel>() {
        @Override
        public FatoraModel createFromParcel(Parcel in) {
            return new FatoraModel(in);
        }

        @Override
        public FatoraModel[] newArray(int size) {
            return new FatoraModel[size];
        }
    };

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(product_name);
        parcel.writeString(product_img);
        parcel.writeString(food_type);
        parcel.writeString(quantity);
        parcel.writeString(price);
    }
}
