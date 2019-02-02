package com.homFood.model;

/**
 * Created by Ma7MouD on 5/6/2018.
 */

public class FamilyModel {

    private String family_id;
    private String family_name;
    private String family_email;
    private String family_img;
    private String family_lat;
    private String family_lan;
    private String family_address;

    public FamilyModel() {
    }

    public FamilyModel(String family_id, String family_name, String family_email, String family_img, String family_lat, String family_lan, String family_address) {
        this.family_id = family_id;
        this.family_name = family_name;
        this.family_email = family_email;
        this.family_img = family_img;
        this.family_lat = family_lat;
        this.family_lan = family_lan;
        this.family_address = family_address;
    }

    public String getFamily_id() {
        return family_id;
    }

    public void setFamily_id(String family_id) {
        this.family_id = family_id;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public String getFamily_email() {
        return family_email;
    }

    public void setFamily_email(String family_email) {
        this.family_email = family_email;
    }

    public String getFamily_img() {
        return family_img;
    }

    public void setFamily_img(String family_img) {
        this.family_img = family_img;
    }

    public String getFamily_lat() {
        return family_lat;
    }

    public void setFamily_lat(String family_lat) {
        this.family_lat = family_lat;
    }

    public String getFamily_lan() {
        return family_lan;
    }

    public void setFamily_lan(String family_lan) {
        this.family_lan = family_lan;
    }

    public String getFamily_address() {
        return family_address;
    }

    public void setFamily_address(String family_address) {
        this.family_address = family_address;
    }
}
