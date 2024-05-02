package com.application.database;

import javafx.scene.chart.XYChart;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

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
        int expectedTotalCount = 16 ; // Fill in the expected total count
        int actualTotalCount = dataManager.selectTotalData(table,"Any","Any","Any","Any","2015-01-01 12:01:02", "2015-01-08 22:11:57");
        assertEquals(expectedTotalCount, actualTotalCount);
    }
    @Test
    public void testSelectTotalDataWithFilter() {
        String table = "clicklog";
        int expectedTotalCount = 5 ; // Fill in the expected total count
        int actualTotalCount = dataManager.selectTotalData(table,"Female",">30","High","News","2015-01-01 12:01:02", "2015-01-08 22:11:57");
        assertEquals(expectedTotalCount, actualTotalCount);
    }

    @Test
    public void testSelectZeroClickCost() {
        int expectedZeroClickCostCount = 3; // Fill in the expected count of zero click cost

        int actualZeroClickCostCount = dataManager.selectZeroClickCost("Any","Any","Any","Any","2015-01-01 12:01:02", "2015-01-08 22:11:57");
        assertEquals(expectedZeroClickCostCount, actualZeroClickCostCount);
    }

    @Test
    public void testSelectAvgData() {
        String column = "clickcost";
        String table = "clicklog";
        int expectedAvgData = 8; // 8.1 rounded down to 8

       int actualAvgData = dataManager.selectAvgData(column, table,"Any","Any","Any","Any","2015-01-01 12:01:02", "2015-01-08 22:11:57");
       assertEquals(expectedAvgData, actualAvgData);
    }

    @Test
    public void testGetUniqueAppearanceInt() {
        String column = "Gender";
        String table = "impressionlog";
        int[] expectedUniqueAppearances = {10, 8}; // Fill in the expected unique appearance counts

        int[] actualUniqueAppearances = dataManager.getUniqueAppearanceInt(column, table,"2015-01-01 12:01:02", "2015-01-08 22:11:57");
        assertEquals(expectedUniqueAppearances.length, actualUniqueAppearances.length);
        for (int i = 0; i < expectedUniqueAppearances.length; i++) {
            assertEquals(expectedUniqueAppearances[i], actualUniqueAppearances[i]);
        }
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
        String table = "impressionlog";
        int expectedUniqueCount = 18; // Fill in the expected unique count

        int actualUniqueCount = dataManager.uniqueValues(column, table);
        assertEquals(expectedUniqueCount, actualUniqueCount);
    }
    @Test
    public void testMaxDate(){
        String table = "clicklog";
        String expectedMaxDate = "2015-01-08 12:11:11";
        String actualMaxDate = dataManager.getMaxDateFromTable(table);
        assertEquals(expectedMaxDate, actualMaxDate);
    }
    @Test
    public void testMinDate(){
        String table = "clicklog";
        String expectedMinDate = "2015-01-08 12:11:11";
        String actualMinDate = dataManager.getMaxDateFromTable(table);
        assertEquals(expectedMinDate, actualMinDate);
    }
    @Test
    public void testGraphTotalClickData(){
        XYChart.Series<String,Number> expectedData = new XYChart.Series<>() ;
        expectedData.getData().addAll(Arrays.asList(
                new XYChart.Data<>("2015-01-01 12:00:00",1),
                new XYChart.Data<>("2015-01-01 13:00:00",4),
                new XYChart.Data<>("2015-01-01 14:00:00",1),
                new XYChart.Data<>("2015-01-02 15:00:00",1),
                new XYChart.Data<>("2015-01-02 16:00:00",1),
                new XYChart.Data<>("2015-01-02 17:00:00",1),
                new XYChart.Data<>("2015-01-02 18:00:00",1),
                new XYChart.Data<>("2015-01-03 19:00:00",1),
                new XYChart.Data<>("2015-01-03 20:00:00",1),
                new XYChart.Data<>("2015-01-03 22:00:00",1),
                new XYChart.Data<>("2015-01-04 12:00:00",1),
                new XYChart.Data<>("2015-01-05 12:00:00",1),
                new XYChart.Data<>("2015-01-08 12:00:00",1)
        ));
        XYChart.Series<String,Number> actualData = dataManager.getData("totalClicks","hour","2015-01-01 12:01:02", "2015-01-08 22:11:57","Any","Any","Any","Any");
        seriesEquals(expectedData, actualData);
    }
    @Test
    public void testGraphTotalUniquesData(){
        XYChart.Series<String,Number> expectedData = new XYChart.Series<>() ;
        expectedData.getData().addAll(Arrays.asList(
                new XYChart.Data<>("2015-01-01 12:00:00",1),
                new XYChart.Data<>("2015-01-01 13:00:00",4),
                new XYChart.Data<>("2015-01-01 14:00:00",1),
                new XYChart.Data<>("2015-01-02 15:00:00",1),
                new XYChart.Data<>("2015-01-02 16:00:00",1),
                new XYChart.Data<>("2015-01-02 17:00:00",1),
                new XYChart.Data<>("2015-01-02 18:00:00",1),
                new XYChart.Data<>("2015-01-03 19:00:00",1),
                new XYChart.Data<>("2015-01-03 20:00:00",1),
                new XYChart.Data<>("2015-01-03 22:00:00",1),
                new XYChart.Data<>("2015-01-04 12:00:00",1),
                new XYChart.Data<>("2015-01-05 12:00:00",1),
                new XYChart.Data<>("2015-01-08 12:00:00",1)
        ));
        XYChart.Series<String,Number> actualData = dataManager.getData("totalUniques","hour","2015-01-01 12:01:02", "2015-01-08 22:11:57","Any","Any","Any","Any");
        seriesEquals(expectedData, actualData);
    }
    @Test
    public void testGraphTotalImpressionData(){
        XYChart.Series<String,Number> expectedData = new XYChart.Series<>() ;
        expectedData.getData().addAll(Arrays.asList(
                new XYChart.Data<>("2015-01-01 12:00:00",1),
                new XYChart.Data<>("2015-01-01 13:00:00",4),
                new XYChart.Data<>("2015-01-01 14:00:00",1),
                new XYChart.Data<>("2015-01-02 15:00:00",1),
                new XYChart.Data<>("2015-01-02 16:00:00",1),
                new XYChart.Data<>("2015-01-02 17:00:00",1),
                new XYChart.Data<>("2015-01-02 18:00:00",1),
                new XYChart.Data<>("2015-01-03 19:00:00",1),
                new XYChart.Data<>("2015-01-03 20:00:00",1),
                new XYChart.Data<>("2015-01-03 22:00:00",1),
                new XYChart.Data<>("2015-01-04 12:00:00",1),
                new XYChart.Data<>("2015-01-05 12:00:00",1),
                new XYChart.Data<>("2015-01-08 12:00:00",1)
        ));
        XYChart.Series<String,Number> actualData = dataManager.getData("totalUniques","hour","2015-01-01 12:01:02", "2015-01-08 22:11:57","Any","Any","Any","Any");
        seriesEquals(expectedData, actualData);
    }
    @Test
    public void testGraphConversionRateData(){
        XYChart.Series<String,Number> expectedData = new XYChart.Series<>() ;
        expectedData.getData().addAll(Arrays.asList(
                new XYChart.Data<>("2015-01-02 17:00:00",1),
                new XYChart.Data<>("2015-01-02 18:00:00",1),
                new XYChart.Data<>("2015-01-03 19:00:00",1),
                new XYChart.Data<>("2015-01-03 20:00:00",1),
                new XYChart.Data<>("2015-01-03 22:00:00",1),
                new XYChart.Data<>("2015-01-04 12:00:00",1),
                new XYChart.Data<>("2015-01-05 12:00:00",1),
                new XYChart.Data<>("2015-01-08 12:00:00",1)
        ));
        XYChart.Series<String,Number> actualData = dataManager.getData("totalConversions","hour","2015-01-01 12:01:02", "2015-01-08 22:11:57","Any","Any","Any","Any");
        seriesEquals(expectedData,actualData);
    }
    @Test
    public void testGraphTotalCostData(){
        XYChart.Series<String,Number> expectedData = new XYChart.Series<>() ;
        expectedData.getData().addAll(Arrays.asList(
                new XYChart.Data<>("2015-01-01 12:00:00",10),
                new XYChart.Data<>("2015-01-01 13:00:00",40),
                new XYChart.Data<>("2015-01-01 14:00:00",10),
                new XYChart.Data<>("2015-01-02 15:00:00",10),
                new XYChart.Data<>("2015-01-02 16:00:00",10),
                new XYChart.Data<>("2015-01-02 17:00:00",10),
                new XYChart.Data<>("2015-01-02 18:00:00",0),
                new XYChart.Data<>("2015-01-03 19:00:00",0),
                new XYChart.Data<>("2015-01-03 20:00:00",0),
                new XYChart.Data<>("2015-01-03 22:00:00",10),
                new XYChart.Data<>("2015-01-04 12:00:00",10),
                new XYChart.Data<>("2015-01-05 12:00:00",10),
                new XYChart.Data<>("2015-01-08 12:00:00",10)
        ));
        XYChart.Series<String,Number> actualData = dataManager.getData("totalCost","hour","2015-01-01 12:01:02", "2015-01-08 22:11:57","Any","Any","Any","Any");
        seriesEquals(expectedData, actualData);
    }
    @Test
    public void testGraphCostPerAcqData(){
        XYChart.Series<String,Number> expectedData = new XYChart.Series<>() ;
        expectedData.getData().addAll(Arrays.asList(
                new XYChart.Data<>("2015-01-02 17:00:00",10.0),
                new XYChart.Data<>("2015-01-02 18:00:00",0.0),
                new XYChart.Data<>("2015-01-03 19:00:00",0.0),
                new XYChart.Data<>("2015-01-03 20:00:00",0.0),
                new XYChart.Data<>("2015-01-03 22:00:00",10.0),
                new XYChart.Data<>("2015-01-04 12:00:00",10.0),
                new XYChart.Data<>("2015-01-05 12:00:00",10.0),
                new XYChart.Data<>("2015-01-08 12:00:00",10.0)
        ));
        XYChart.Series<String,Number> actualData = dataManager.getData("costPerAcq","hour","2015-01-01 12:01:02", "2015-01-08 22:11:57","Any","Any","Any","Any");
        seriesEquals(expectedData, actualData);
    }


    private void seriesEquals(XYChart.Series<String, Number> series1, XYChart.Series<String, Number> series2) {
        if (series1.getData().size() != series2.getData().size()) {
            assertEquals(1,2);; // The series have different sizes, so they cannot be equal
        }

        for (int i = 0; i < series1.getData().size(); i++) {
            XYChart.Data<String, Number> data1 = series1.getData().get(i);
            XYChart.Data<String, Number> data2 = series2.getData().get(i);
            System.out.println(data1.getYValue());
            System.out.println(data2.getYValue());
            assertEquals(data1.getYValue(),data2.getYValue());
            if ((!data1.getXValue().equals(data2.getXValue()) || (!(data1.getYValue() == data2.getYValue())))){
                System.out.println("Hello");
                assertEquals(1,2); // Found a mismatch, series are not equal

            }
        }

        assertEquals(1,1);; // All points matched
    }



}