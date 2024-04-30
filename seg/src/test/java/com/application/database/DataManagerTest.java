package com.application.database;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataManagerTest {
    private DbConnection dbc = new DbConnection();
    private DataManager dataManager = new DataManager();
    public void setUp() {
        // Initialize the DataManager instance or any necessary setup
        dataManager = new DataManager();
    }

    @Test
    public void testSelectTotalData() {
        String table = "clicklog";
        int expectedTotalCount = 40159 ; // Fill in the expected total count
        int actualTotalCount = dataManager.selectTotalData(table);
        assertEquals(expectedTotalCount, actualTotalCount);
    }

    @Test
    public void testSelectZeroClickCost() {
        int expectedZeroClickCostCount = 20327; // Fill in the expected count of zero click cost

        int actualZeroClickCostCount = dataManager.selectZeroClickCost();
        assertEquals(expectedZeroClickCostCount, actualZeroClickCostCount);
    }

    @Test
    public void testSelectAvgData() {
        String column = "clickcost";
        String table = "clicklog";
        int expectedAvgData = 4; // Fill in the expected average data value

        int actualAvgData = dataManager.selectAvgData(column, table);
        assertEquals(expectedAvgData, actualAvgData);
    }

    @Test
    public void testGetUniqueAppearanceInt() {
//        String column = "Gender";
//        String table = "impressionlog";
//        int[] expectedUniqueAppearances = {161469, 324635}; // Fill in the expected unique appearance counts
//
//        int[] actualUniqueAppearances = dataManager.getUniqueAppearanceInt(column, table);
//        assertEquals(expectedUniqueAppearances.length, actualUniqueAppearances.length);
//        for (int i = 0; i < expectedUniqueAppearances.length; i++) {
//            assertEquals(expectedUniqueAppearances[i], actualUniqueAppearances[i]);
//        }
    }

    @Test
    public void testGetUniqueAppearanceString() {
        String column = "Gender";
        String table = "impressionlog";
        String[] expectedUniqueAppearances = {"Male" , "Female"}; // Fill in the expected unique appearance strings

        String[] actualUniqueAppearances = dataManager.getUniqueAppearanceString(column, table);
        assertEquals(expectedUniqueAppearances.length, actualUniqueAppearances.length);
        for (int i = 0; i < expectedUniqueAppearances.length; i++) {
            assertEquals(expectedUniqueAppearances[i], actualUniqueAppearances[i]);
        }
    }


    @Test
    public void testUniqueValues() {
        String column = "id";
        String table = "clicklog";
        int expectedUniqueCount = 23806; // Fill in the expected unique count

        int actualUniqueCount = dataManager.uniqueValues(column, table);
        assertEquals(expectedUniqueCount, actualUniqueCount);
    }
}