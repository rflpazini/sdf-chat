package com.rflpazini.sdf.model;

/**
 * Created by rflpazini on 10/28/16.
 */

public class User {

    private int id;
    private String userName;
    private String userToken;

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
