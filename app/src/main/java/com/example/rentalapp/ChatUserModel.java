package com.example.rentalapp;

public class ChatUserModel {
    String tenantid, documentid, renterid, chatid, chatroomname;

    public ChatUserModel() {
    }

    public String getTenantid() {
        return tenantid;
    }

    public void setTenantid(String tenantid) {
        this.tenantid = tenantid;
    }

    public String getDocumentid() {
        return documentid;
    }

    public void setDocumentid(String documentid) {
        this.documentid = documentid;
    }

    public String getRenterid() {
        return renterid;
    }

    public void setRenterid(String renterid) {
        this.renterid = renterid;
    }

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public String getChatroomname() {
        return chatroomname;
    }

    public void setChatroomname(String chatroomname) {
        this.chatroomname = chatroomname;
    }
}
