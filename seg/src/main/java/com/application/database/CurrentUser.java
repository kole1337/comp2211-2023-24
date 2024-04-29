package com.application.database;

public class CurrentUser {
    String currentUser = "";

    public CurrentUser(String username){
        setUser(username);
    }

    private void setUser(String username){
        this.currentUser = username;
    }
    public String getUser(){
        return this.currentUser;
    }
}
