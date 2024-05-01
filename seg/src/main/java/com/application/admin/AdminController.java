package com.application.admin;

import com.application.database.DataManager;
import com.application.database.DbConnection;
import com.application.database.UserManager;
import com.application.logger.LogAction;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AdminController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Logger logger = Logger.getLogger(getClass().getName());
    DbConnection db = new DbConnection();

    UserManager userManager = new UserManager();
    User user = null;
    LogAction logAction = new LogAction();

    @FXML
    private TableView<User> showUsers = new TableView<>();
    @FXML
    private TableColumn<User, String> fName;

    @FXML
    private TableColumn<User, String> password;
    @FXML
    private TableColumn<User, String> surName;

    @FXML
    private TableColumn<User, String> userName;
    @FXML
    private TableColumn<User, Integer> userId;

    @FXML
    private TableColumn<User, String> editCol;


    public AdminController() throws Exception{

    }

    @FXML
    public void initialize() throws Exception{
        logAction.logActionToFile("Opening Admin Panel.");
    }


    /**
     * Function that logs the user out to the login panel.
     * It invokes an alert and waits for the user to confirm the exit.
     * */
    public void logoutFunc(ActionEvent event) {
        Alert a = new Alert(Alert.AlertType.WARNING, "Signing out!", ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = a.showAndWait();
        if(result.isPresent() && result.get()==ButtonType.OK) {
            try {
                db.resetConn();
                logAction.logActionToFile("Logging out of admin panel.");
                root = FXMLLoader.load(getClass().getResource("/com/application/login/hello-view.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


                scene = new Scene(root);
                stage.setScene(scene);

                stage.show();
                logger.log(Level.INFO, "Opening hello view.");


            } catch (Exception e) {
                logger.log(Level.SEVERE, "Failed to create new Window.", e);
                logAction.logActionToFile("Error logging out: " + e);
                Alert a1 = new Alert(Alert.AlertType.WARNING, "Error logging out: " + e);
                a1.show();
            }
        }
    }

    ObservableList<User> UserList = FXCollections.observableArrayList();

    /**
     * Function that refreshes the table of users
     * */
    @FXML
    private void refreshTable() {
        for ( int i = 0; i<showUsers.getItems().size(); i++) {
            showUsers.getItems().clear();
        }
        System.out.println("Entering");
        userName.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        password.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        userId.setCellValueFactory(new PropertyValueFactory<User, Integer>("user_id"));

        try {
            UserList.clear();
            ResultSet rs = userManager.selectAll();
            System.out.println("Populations");
            while (rs.next()) {
                UserList.add(new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("user_id")));

//                System.out.println(UserList.get(1));
            }
            showUsers.setItems(UserList);
            logAction.logActionToFile("Refreshing table with users.");
            logger.log(Level.SEVERE, "Refreshing table with users.");

        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "Error refreshing table: ", e);
            logAction.logActionToFile("Error refreshing table " + e);
            Alert a1 = new Alert(Alert.AlertType.WARNING, "Error refreshing table: " + e);
            a1.show();
        }


    }

    /**
     * Function for the delete button
     * that gets the selection from the table
     * and deletes that user
     * */
    @FXML
    private void deleteSelected(){
        TableView.TableViewSelectionModel<User> selectionModel = showUsers.getSelectionModel();
        ObservableList<Integer> list = selectionModel.getSelectedIndices();
        if(list.size() != 0) {
            try {
                user = showUsers.getSelectionModel().getSelectedItem();
                UserManager um = new UserManager();
                um.deleteUserWithID(user.getUser_id());

                Integer[] selectedIndice = new Integer[list.size()];
                selectedIndice = list.toArray(selectedIndice);
                showUsers.getItems().remove(selectedIndice[0].intValue());
                logger.log(Level.INFO, "Deleting a user.");
                logAction.logActionToFile("Deleting a user from table.");

            }catch(Exception e){
                logger.log(Level.WARNING,"Error deleting user: " + e);
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Error deleting user: " + e);
                a.show();
                logAction.logActionToFile("Error deleting a user.");
                e.printStackTrace();
            }
        }else{
            logAction.logActionToFile("No user was selecting for deletion.");
            Alert a = new Alert(Alert.AlertType.INFORMATION, "No user is selected.");
            a.show();
            logger.log(Level.INFO,"No user was selected for deletion.");

        }
        
    }

    /**
     * Function for the Edit button
     * that takes the selected user and
     * opens the editUser panel, and sends the details
     * of the selected user to the editUser panel
     * */
    @FXML
    void editSelected(MouseEvent event) {
        TableView.TableViewSelectionModel<User> selectionModel = showUsers.getSelectionModel();
        if(showUsers.getSelectionModel().getSelectedItem() != null) {
            try {
                User user = showUsers.getSelectionModel().getSelectedItem();
                editUserController obj = new editUserController();
                obj.openEditPanel(user.getUsername(), user.getPassword(), user.getUser_id());
            }catch(Exception e){
                logAction.logActionToFile("Error editing user: " + e);
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Error editing user: " + e);
                a.show();
                logger.log(Level.WARNING,"Error editing user: " + e);
                e.printStackTrace();
            }
        }else{
            Alert a = new Alert(Alert.AlertType.INFORMATION, "No user is selected");
            a.show();
            logger.log(Level.WARNING,"No user is selected");
            logAction.logActionToFile("No user is selected");
        }
    }

    /**
     * Simply load the users in the table from the database
     * */
    public void loadUsers(){
        populateTableView();
    }

    private void populateTableView() {
        for ( int i = 0; i<showUsers.getItems().size(); i++) {
            showUsers.getItems().clear();
        }
        System.out.println("Entering");
        userName.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        password.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        userId.setCellValueFactory(new PropertyValueFactory<User, Integer>("user_id"));
        try {

            ResultSet rs = userManager.selectAll();
            System.out.println("Populations");
            while (rs.next()) {
                String fName = rs.getString("username");
                String surName = rs.getString("password");
                Integer userid = rs.getInt("user_id");
                showUsers.getItems().add(new User(fName, surName,userid));
                System.out.println(fName + ", " + surName);
            }
            logger.log(Level.INFO,"Loading users into table.");
            logAction.logActionToFile("Loading users into table.");
        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Error population table: " + e);
            a.show();
            logger.log(Level.WARNING,"Error population table: " + e);
            logAction.logActionToFile("Error population table: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Function to create a new user.
     * It opens the createNewUser panel which has its own
     * controller {@link com.application.admin.addUserController}
     * */
    public void createNewUser() {
        try {
            logger.log(Level.WARNING,"Creating new user.");
            logAction.logActionToFile("Creating new user.");
            Parent parent = FXMLLoader.load(getClass().getResource("createNewUser.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        }catch(Exception e){
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Error creating new user: " + e);
            a.show();
            logger.log(Level.WARNING,"Error creating new user: " + e);
            logAction.logActionToFile("Error creating new user: " + e);
            e.printStackTrace();
        }
    }
}