package com.application.admin;

import com.application.database.DbConnection;
import com.application.database.UserManager;
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
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

public class AdminController {
    public TextField getDBname;
    public PasswordField getDBpassword;
    public TextField getDBusername;
    @FXML
    public Label statusLabel;
    @FXML
    public Button checkConnectionDB;
    public TextField passwordField;
    public TextField userIdField;
    public TextField usernameField;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Logger logger = Logger.getLogger(getClass().getName());
    DbConnection db = new DbConnection();

    UserManager userManager = new UserManager();
    User user = null;

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

    ObservableList<User> UserList = FXCollections.observableArrayList();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @FXML
    private void deleteSelected(){
        TableView.TableViewSelectionModel<User> selectionModel = showUsers.getSelectionModel();

        ObservableList<Integer> list = selectionModel.getSelectedIndices();
        Integer[] selectedIndice = new Integer[list.size()];
        selectedIndice = list.toArray(selectedIndice);
        showUsers.getItems().remove(selectedIndice[0].intValue());
        
    }

    @FXML
    void editSelected(MouseEvent event) {
        TableView.TableViewSelectionModel<User> selectionModel = showUsers.getSelectionModel();
        User user = showUsers.getSelectionModel().getSelectedItem();

        editUserController obj = new editUserController();

        obj.openEditPanel(user.getUsername(),user.getPassword(), user.getUser_id());
    }

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

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createNewUser() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("createNewUser.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
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
