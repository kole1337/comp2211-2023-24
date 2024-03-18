package com.application.admin;

import com.application.database.DbConnection;
import com.application.database.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminController {
    public TextField getDBname;
    public PasswordField getDBpassword;
    public TextField getDBusername;
    @FXML
    public Label statusLabel;
    @FXML
    public Button checkConnectionDB;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Logger logger = Logger.getLogger(getClass().getName());
    DbConnection db = new DbConnection();

    UserManager userManager = new UserManager();

    @FXML
    private TableColumn<User, String> fName;

    @FXML
    private TableColumn<User, String> password;

    @FXML
    private TableView<User> showUsers = new TableView<>();

    @FXML
    private TableColumn<User, String> surName;

    @FXML
    private TableColumn<User, String> userName;

    public AdminController() throws Exception{

    }

    @FXML
    public void initialize() throws Exception{
        if(db.checkConn()) statusLabel.setText("Status: connected");
    }

    public void logoutFunc(ActionEvent event) {
        Alert a = new Alert(Alert.AlertType.WARNING, "Signing out!", ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = a.showAndWait();
        if(result.isPresent() && result.get()==ButtonType.OK) {
            try {
                root = FXMLLoader.load(getClass().getResource("/com/application/login/hello-view.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


                scene = new Scene(root);
                stage.setScene(scene);

                stage.show();
                logger.log(Level.INFO, "Opening hello view.");
            } catch (IOException e) {
                logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Failed to create new Window.", e);
            }
        }
    }

    public void loadUsers(){
        userName.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        password.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        populateTableView();
    }

    private void populateTableView() {
        System.out.println("Entering");

        try {

            ResultSet rs = userManager.selectAll();
            System.out.println("Populations");
            while (rs.next()) {
                String fName = rs.getString("username");
                String surName = rs.getString("password");
                showUsers.getItems().add(new User(fName, surName));
                System.out.println(fName + ", " + surName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createNewUser() {
        logger.log(Level.INFO, "Creating new user.");
        TextInputDialog tiDialog = new TextInputDialog();
        tiDialog.setTitle("New user");
        tiDialog.setHeaderText("Input the username and password");
        tiDialog.setContentText("Username: ");
//        tiDialog.setContentText("Password: ");

        tiDialog.showAndWait();
        String result = tiDialog.getResult().toString();
        if(result.isEmpty()){
            Alert a = new Alert(Alert.AlertType.WARNING, "Insert name!" ,ButtonType.OK, ButtonType.CANCEL);
            a.showAndWait();
            if(a.getResult() == ButtonType.OK){
                createNewUser();
            }
        }
    }

    public void connectToDB(ActionEvent actionEvent) throws Exception {
        logger.log(Level.INFO, "Establishing connection with db.");
        if(getDBname.getText().isEmpty() || getDBpassword.getText().isEmpty()){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.show();
        }else {
            String user = getDBusername.getText();
            String password = getDBpassword.getText();
            //String dbName = getDBname.getText();

            //db.makeConn(user, password);
            createOrUpdateFile("seg/src/main/resources/user.txt", user, password);
            statusLabel.setText("Status: connected");
        }

    }

    @FXML
    Label test = new Label();
    public void checkConnection(){
        try {
            db.checkConn();
            logger.log(Level.INFO, "Connected with db.");
            statusLabel.setText("Status: connected");
           // System.out.println("Connceted!");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void createOrUpdateFile(String filePath, String username, String password) {
        try {
            File file = new File(filePath);
            FileWriter writer = new FileWriter(file);
            BufferedWriter buffer = new BufferedWriter(writer);
            buffer.write(username);
            buffer.newLine();
            buffer.write(password);
            buffer.close();
            System.out.println("File created/updated successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }


}
