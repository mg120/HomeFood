package com.homFood.model;

public class CategoriesModel {

    private String id ;
    private String Name ;
    private String img ;

    public CategoriesModel(String id, String name, String img) {
        this.id = id;
        Name = name;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
