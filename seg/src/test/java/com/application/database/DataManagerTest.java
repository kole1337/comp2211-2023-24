package com.application.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataManagerTest {
    DataManager dataman = new DataManager();
    @Test
    void getGenderTest(){
        ObservableList<String> genders = FXCollections.observableArrayList();
        genders.add("Male");
        genders.add("Female");
        assertEquals(genders,dataman.getGenders());
    }

}