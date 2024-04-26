package com.application.admin;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    //private final StringProperty firstName;
    private StringProperty password;
    private StringProperty username;
    private int user_id;
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id =(user_id);
    }



    public User( String user, String pass, int id) {
        this.username = new SimpleStringProperty(user);
        this.password = new SimpleStringProperty(pass);
        this.user_id = id;
    }

//    public String getFirstName() {
//        return firstName.get();
//    }
//
//    public StringProperty firstNameProperty() {
//        return firstName;
//    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }
}
