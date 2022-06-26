package com.faislll.myapplication.model;

public class Users {
    private String id_user, email, name;

    public Users(String id_user, String email, String name) {
        this.id_user = id_user;
        this.email = email;
        this.name = name;
    }

    public Users() {
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
