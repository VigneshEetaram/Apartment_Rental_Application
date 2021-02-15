package com.example.rentalapp;

public class User {
    public String fullname,emails,phonenumber,userids;

    public User() {

    }

    public User(String fullname, String emails, String phonenumber, String userids) {
        this.fullname = fullname;
        this.emails = emails;
        this.phonenumber = phonenumber;
        this.userids = userids;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUserids() {
        return userids;
    }

    public void setUserids(String userids) {
        this.userids = userids;
    }
}
