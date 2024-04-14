package com.example.lensleap.datamodel;

public class UserModel {
    private String img, usename, email,password;

    public UserModel(String usename, String email, String password) {
        this.usename = usename;
        this.email = email;
        this.password = password;
    }

    public UserModel(String img, String usename, String email, String password) {
        this.img = img;
        this.usename = usename;
        this.email = email;
        this.password = password;
    }
}
