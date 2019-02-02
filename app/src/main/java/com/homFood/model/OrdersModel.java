package com.homFood.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ma7MouD on 4/25/2018.
 */

public class OrdersModel implements Parcelable {

    private String order_img ;
    private String order_name ;
    private String order_foodtype ;
    private String order_price ;
    private String order_quantity ;
    private String order_id ;
    private String state_type ;
    private String Seller_id ;
    private String Seller_name ;
    private String Seller_mail ;
    private String Customer_id ;
    private String Customer_name ;
    private String Customer_mail ;
    private String Customer_phone ;

    public OrdersModel() {
    }

    public OrdersModel(String order_img, String order_name, String order_foodtype, String order_price, String order_quantity, String order_id, String state_type, String seller_id, String seller_name, String seller_mail, String customer_id, String customer_name, String customer_mail, String customer_phone) {
        this.order_img = order_img;
        this.order_name = order_name;
        this.order_foodtype = order_foodtype;
        this.order_price = order_price;
        this.order_quantity = order_quantity;
        this.order_id = order_id;
        this.state_type = state_type;
        Seller_id = seller_id;
        Seller_name = seller_name;
        Seller_mail = seller_mail;
        Customer_id = customer_id;
        Customer_name = customer_name;
        Customer_mail = customer_mail;
        Customer_phone = customer_phone;
    }


    protected OrdersModel(Parcel in) {
        order_img = in.readString();
        order_name = in.readString();
        order_foodtype = in.readString();
        order_price = in.readString();
        order_quantity = in.readString();
        order_id = in.readString();
        state_type = in.readString();
        Seller_id = in.readString();
        Seller_name = in.readString();
        Seller_mail = in.readString();
        Customer_id = in.readString();
        Customer_name = in.readString();
        Customer_mail = in.readString();
        Customer_phone = in.readString();
    }

    public static final Creator<OrdersModel> CREATOR = new Creator<OrdersModel>() {
        @Override
        public OrdersModel createFromParcel(Parcel in) {
            return new OrdersModel(in);
        }

        @Override
        public OrdersModel[] newArray(int size) {
            return new OrdersModel[size];
        }
    };

    public String getOrder_foodtype() {
        return order_foodtype;
    }

    public void setOrder_foodtype(String order_foodtype) {
        this.order_foodtype = order_foodtype;
    }

    public String getSeller_id() {
        return Seller_id;
    }

    public void setSeller_id(String seller_id) {
        Seller_id = seller_id;
    }

    public String getSeller_name() {
        return Seller_name;
    }

    public void setSeller_name(String seller_name) {
        Seller_name = seller_name;
    }

    public String getSeller_mail() {
        return Seller_mail;
    }

    public void setSeller_mail(String seller_mail) {
        Seller_mail = seller_mail;
    }

    public String getCustomer_id() {
        return Customer_id;
    }

    public void setCustomer_id(String customer_id) {
        Customer_id = customer_id;
    }

    public String getCustomer_name() {
        return Customer_name;
    }

    public void setCustomer_name(String customer_name) {
        Customer_name = customer_name;
    }

    public String getCustomer_mail() {
        return Customer_mail;
    }

    public void setCustomer_mail(String customer_mail) {
        Customer_mail = customer_mail;
    }

    public String getCustomer_phone() {
        return Customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        Customer_phone = customer_phone;
    }

    public String getOrder_quantity() {
        return order_quantity;
    }

    public void setOrder_quantity(String order_quantity) {
        this.order_quantity = order_quantity;
    }

    public String getState_type() {
        return state_type;
    }

    public void setState_type(String state_type) {
        this.state_type = state_type;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_price() {
        return order_price;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }

    public String getOrder_img() {
        return order_img;
    }

    public void setOrder_img(String order_img) {
        this.order_img = order_img;
    }

    public String getOrder_name() {
        return order_name;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(order_img);
        parcel.writeString(order_name);
        parcel.writeString(order_foodtype);
        parcel.writeString(order_price);
        parcel.writeString(order_quantity);
        parcel.writeString(order_id);
        parcel.writeString(state_type);
        parcel.writeString(Seller_id);
        parcel.writeString(Seller_name);
        parcel.writeString(Seller_mail);
        parcel.writeString(Customer_id);
        parcel.writeString(Customer_name);
        parcel.writeString(Customer_mail);
        parcel.writeString(Customer_phone);
    }
}
