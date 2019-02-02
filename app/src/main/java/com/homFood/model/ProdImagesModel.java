package com.homFood.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ma7MouD on 4/12/2018.
 */

public class ProdImagesModel implements Parcelable {

    private String prod_img1 ;
    private String prod_img2 ;
    private String prod_img3 ;
    private String prod_img4;
    private String prod_img5;

    public ProdImagesModel(String prod_img1, String prod_img2, String prod_img3, String prod_img4, String prod_img5) {
        this.prod_img1 = prod_img1;
        this.prod_img2 = prod_img2;
        this.prod_img3 = prod_img3;
        this.prod_img4 = prod_img4;
        this.prod_img5 = prod_img5;
    }

    protected ProdImagesModel(Parcel in) {
        prod_img1 = in.readString();
        prod_img2 = in.readString();
        prod_img3 = in.readString();
        prod_img4 = in.readString();
        prod_img5 = in.readString();
    }

    public static final Creator<ProdImagesModel> CREATOR = new Creator<ProdImagesModel>() {
        @Override
        public ProdImagesModel createFromParcel(Parcel in) {
            return new ProdImagesModel(in);
        }

        @Override
        public ProdImagesModel[] newArray(int size) {
            return new ProdImagesModel[size];
        }
    };

    public String getProd_img1() {
        return prod_img1;
    }

    public void setProd_img1(String prod_img1) {
        this.prod_img1 = prod_img1;
    }

    public String getProd_img2() {
        return prod_img2;
    }

    public void setProd_img2(String prod_img2) {
        this.prod_img2 = prod_img2;
    }

    public String getProd_img3() {
        return prod_img3;
    }

    public void setProd_img3(String prod_img3) {
        this.prod_img3 = prod_img3;
    }

    public String getProd_img4() {
        return prod_img4;
    }

    public void setProd_img4(String prod_img4) {
        this.prod_img4 = prod_img4;
    }

    public String getProd_img5() {
        return prod_img5;
    }

    public void setProd_img5(String prod_img5) {
        this.prod_img5 = prod_img5;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(prod_img1);
        parcel.writeString(prod_img2);
        parcel.writeString(prod_img3);
        parcel.writeString(prod_img4);
        parcel.writeString(prod_img5);
    }
}
