package com.homFood.model;

public class CouponsModel {

    private String prod_id;
    private String discont_val;
    private String code;
    private String expire_date;

    public CouponsModel(String prod_id, String discont_val, String code, String expire_date) {
        this.prod_id = prod_id;
        this.discont_val = discont_val;
        this.code = code;
        this.expire_date = expire_date;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getDiscont_val() {
        return discont_val;
    }

    public void setDiscont_val(String discont_val) {
        this.discont_val = discont_val;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(String expire_date) {
        this.expire_date = expire_date;
    }
}
