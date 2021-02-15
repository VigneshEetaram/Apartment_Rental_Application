package com.example.rentalapp;

public class Model {
    String image;
    String name;
    String price;
    String description;
    String id;
    String place;
    String email;
    String userid;

    public Model() {
    }

    public Model(String image, String name, String price, String description, String id, String place, String email, String userid) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.description = description;
        this.id = id;
        this.place = place;
        this.email = email;
        this.userid = userid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}