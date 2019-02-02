package com.homFood.model;

import android.os.Parcel;
import android.os.Parcelable;

public class HomeModel implements Parcelable {

    private String product_id;
    private String meal_image;
    private String family_id;
    private String family_name;
    private String food_type;
    private String family_rate;
    private String price;
    private String desc;
    private String time;
    private String seller_id;
    private String seller_name;
    private String seller_email;
    private String address;


    public HomeModel() {
    }

    public HomeModel(String meal_image, String family_id, String family_name, String food_type, String family_rate, String price, String desc, String time, String seller_id, String seller_name, String seller_email) {
        this.meal_image = meal_image;
        this.family_id = family_id;
        this.family_name = family_name;
        this.food_type = food_type;
        this.family_rate = family_rate;
        this.price = price;
        this.desc = desc;
        this.time = time;
        this.seller_id = seller_id;
        this.seller_name = seller_name;
        this.seller_email = seller_email;
    }

    public HomeModel(String product_id, String meal_image, String family_id, String family_name, String food_type, String family_rate, String price, String desc, String time, String seller_id, String seller_name, String seller_email, String address) {
        this.product_id = product_id;
        this.meal_image = meal_image;
        this.family_id = family_id;
        this.family_name = family_name;
        this.food_type = food_type;
        this.family_rate = family_rate;
        this.price = price;
        this.desc = desc;
        this.time = time;
        this.seller_id = seller_id;
        this.seller_name = seller_name;
        this.seller_email = seller_email;
        this.address = address;
    }

    public HomeModel(String meal_image, String family_id, String family_name, String food_type, String family_rate, String price, String desc, String time, String seller_id, String seller_name, String seller_email, String address) {
        this.meal_image = meal_image;
        this.family_id = family_id;
        this.family_name = family_name;
        this.food_type = food_type;
        this.family_rate = family_rate;
        this.price = price;
        this.desc = desc;
        this.time = time;
        this.seller_id = seller_id;
        this.seller_name = seller_name;
        this.seller_email = seller_email;
        this.address = address;
    }


    protected HomeModel(Parcel in) {
        product_id = in.readString();
        meal_image = in.readString();
        family_id = in.readString();
        family_name = in.readString();
        food_type = in.readString();
        family_rate = in.readString();
        price = in.readString();
        desc = in.readString();
        time = in.readString();
        seller_id = in.readString();
        seller_name = in.readString();
        seller_email = in.readString();
        address = in.readString();
    }

    public static final Creator<HomeModel> CREATOR = new Creator<HomeModel>() {
        @Override
        public HomeModel createFromParcel(Parcel in) {
            return new HomeModel(in);
        }

        @Override
        public HomeModel[] newArray(int size) {
            return new HomeModel[size];
        }
    };

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_email() {
        return seller_email;
    }

    public void setSeller_email(String seller_email) {
        this.seller_email = seller_email;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFamily_id() {
        return family_id;
    }

    public void setFamily_id(String family_id) {
        this.family_id = family_id;
    }

    public String getMeal_image() {
        return meal_image;
    }

    public void setMeal_image(String meal_image) {
        this.meal_image = meal_image;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public String getFamily_rate() {
        return family_rate;
    }

    public void setFamily_rate(String family_rate) {
        this.family_rate = family_rate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(product_id);
        parcel.writeString(meal_image);
        parcel.writeString(family_id);
        parcel.writeString(family_name);
        parcel.writeString(food_type);
        parcel.writeString(family_rate);
        parcel.writeString(price);
        parcel.writeString(desc);
        parcel.writeString(time);
        parcel.writeString(seller_id);
        parcel.writeString(seller_name);
        parcel.writeString(seller_email);
        parcel.writeString(address);
    }
}
