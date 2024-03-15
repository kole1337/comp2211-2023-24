package com.application.admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    //private final StringProperty firstName;
    private final StringProperty password;
    private final StringProperty username;

    public User( String user, String pass) {
        this.username = new SimpleStringProperty(user);
        this.password = new SimpleStringProperty(pass);
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
